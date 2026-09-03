package org.company.showbookingapp.payment;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.company.showbookingapp.booking.Booking;
import org.company.showbookingapp.booking.BookingRepository;
import org.company.showbookingapp.booking.BookingSeatRepository;
import org.company.showbookingapp.booking.BookingStatus;
import org.company.showbookingapp.exception.ResourceNotFoundException;
import org.company.showbookingapp.payment.gomobi.GoMobiClient;
import org.company.showbookingapp.payment.gomobi.dto.GoMobiBankResponseDTO;
import org.company.showbookingapp.payment.gomobi.dto.GoMobiPaymentRequestDTO;
import org.company.showbookingapp.payment.gomobi.dto.GoMobiPaymentResponseDTO;
import org.company.showbookingapp.payment.gomobi.dto.GoMobiStatusResponseDTO;
import org.company.showbookingapp.showSeat.ShowSeatStatus;
import org.company.showbookingapp.user.User;
import org.company.showbookingapp.user.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final GoMobiClient goMobiClient;
    private final PaymentRepository paymentRepository;
    private final BookingSeatRepository bookingSeatRepository;

    public GoMobiBankResponseDTO getAvailableBanks() {

        return goMobiClient
                .getAvailableBanks()
                .getBody();
    }

    private String buildPaymentForm(GoMobiPaymentRequestDTO request) {

        return """
            <!DOCTYPE html>
            <html>
            <head>
                <title>Redirecting to FPX</title>
            </head>
            <body>

                <form id="fpxForm"
                      action="https://m-uat.gomobi.io/GoPayNet/api/v1/payments"
                      method="post">

                    <input type="hidden" name="amount" value="%s">
                    <input type="hidden" name="redirectUrl" value="%s">
                    <input type="hidden" name="sellerOrderNo" value="%s">
                    <input type="hidden" name="bankType" value="%s">
                    <input type="hidden" name="mid" value="%s">
                    <input type="hidden" name="buyerName" value="%s">
                    <input type="hidden" name="tid" value="%s">
                    <input type="hidden" name="merchantName" value="%s">
                    <input type="hidden" name="bank" value="%s">
                    <input type="hidden" name="service" value="%s">
                    <input type="hidden" name="email" value="%s">
                    <input type="hidden" name="subMID" value="%s">
                    <input type="hidden" name="checkSum" value="%s">

                </form>

                <script>
                    document.getElementById("fpxForm").submit();
                </script>

            </body>
            </html>
            """.formatted(
                request.getAmount(),
                request.getRedirectUrl(),
                request.getSellerOrderNo(),
                request.getBankType(),
                request.getMid(),
                request.getBuyerName(),
                request.getTid(),
                request.getMerchantName(),
                request.getBank(),
                request.getService(),
                request.getEmail(),
                request.getSubMID(),
                request.getCheckSum()
        );
    }

    public String createPayment(PaymentRequestDTO request,String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Booking booking = bookingRepository.findById(request.getBookingId())
                .orElseThrow(() ->new ResourceNotFoundException("Booking not found"));

        if (!booking.getUser().getId().equals(user.getId())) {
            throw new ResourceNotFoundException("Booking not found");
        }

        String amount = booking.getTotalAmount().toPlainString();

        String sellerOrderNo = "INV-" + booking.getId();

        Payment payment = Payment.builder()
                .booking(booking)
                .amount(booking.getTotalAmount())
                .currency("MYR")
                .transactionId(sellerOrderNo)
                .status(PaymentStatus.PENDING)
                .paymentMethod(PaymentMethod.FPX)
                .createdAt(LocalDateTime.now())
                .build();

        paymentRepository.save(payment);

        return generatePaymentForm(
                sellerOrderNo,
                request.getBank(),
                user
        );
    }

    public String generatePaymentForm(
            String sellerOrderNo,
            String bank,
            User user) {

        Payment payment = paymentRepository
                .findByTransactionId(sellerOrderNo)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Payment not found"));

        Booking booking = payment.getBooking();

        if (!booking.getUser().getId().equals(user.getId())) {
            throw new ResourceNotFoundException("Payment not found");
        }

        String amount = payment.getAmount().toPlainString();

        String checkSum = goMobiClient.generateCheckSum(
                amount,
                sellerOrderNo,
                goMobiClient.getSubMID()
        );

        GoMobiPaymentRequestDTO paymentRequest =
                new GoMobiPaymentRequestDTO(
                        amount,
                        "http://localhost:8080/api/payments/response",
                        sellerOrderNo,
                        goMobiClient.getBankType(),
                        goMobiClient.getMid(),
                        user.getUsername(),
                        goMobiClient.getTid(),
                        goMobiClient.getMerchantName(),
                        bank,
                        goMobiClient.getService(),
                        user.getEmail(),
                        goMobiClient.getSubMID(),
                        checkSum
                );

        return goMobiClient
                .initiateDeposit(paymentRequest)
                .getBody();
    }


    public String checkPaymentStatus(Long bookingId, User user) {

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Booking not found"));

        if (!booking.getUser().getId().equals(user.getId())) {
            throw new ResourceNotFoundException("Booking not found");
        }

        String sellerOrderNo = "INV-" + booking.getId();

        GoMobiStatusResponseDTO response =
                goMobiClient.checkPaymentStatus(sellerOrderNo).getBody();

        if (response == null) {
            return "No response received from GoMobi";
        }

        if (!"0000".equals(response.getResponseCode())) {
            return response.getResponseMessage()
                    + ": "
                    + response.getResponseDescription();
        }

        if (response.getResponseData() == null
                || response.getResponseData().getForSettlement() == null) {
            return "Transaction details not found";
        }

        return response.getResponseData()
                .getForSettlement()
                .getStatus();
    }

    @Transactional
    public void processPaymentResponse(GoMobiPaymentResponseDTO response) {

        if (!"00".equals(response.getFpx_debitAuthCode())
                || !"Approved".equalsIgnoreCase(response.getFpx_debitAuthCodeString())) {
            return;
        }

        String sellerOrderNo = response.getFpx_sellerOrderNo();

        Payment payment = paymentRepository
                .findByBooking_Id(Long.valueOf(
                        sellerOrderNo.replace("INV", "").replace("_MO", "")
                ))
                .orElseThrow(() ->
                        new ResourceNotFoundException("Payment not found"));

        payment.setStatus(PaymentStatus.SUCCESS);
        payment.setTransactionId(response.getFpx_fpxTxnId());
        payment.setUpdatedAt(LocalDateTime.now());

        paymentRepository.save(payment);

        Booking booking = payment.getBooking();
        booking.setStatus(BookingStatus.CONFIRMED);

        bookingRepository.save(booking);
        bookingSeatRepository.findByBookingId(booking.getId())
                .forEach(bookingSeat -> {
                    bookingSeat.getShowSeat().setStatus(ShowSeatStatus.BOOKED);
                });
    }
}
