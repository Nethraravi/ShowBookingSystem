package org.company.showbookingapp.payment;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.company.showbookingapp.payment.gomobi.dto.GoMobiIPNResponseDTO;
import org.company.showbookingapp.payment.gomobi.dto.GoMobiPaymentResponseDTO;
import org.company.showbookingapp.user.User;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.company.showbookingapp.payment.gomobi.dto.GoMobiBankResponseDTO;
import org.company.showbookingapp.security.CustomUserDetails;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tools.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/payments")
@SecurityRequirement(name = "Bearer Authentication")
public class PaymentController {

    private final PaymentService paymentService;
    private final ObjectMapper objectMapper;

    @GetMapping("/banks")
    public ResponseEntity<GoMobiBankResponseDTO> getAvailableBanks() {

        return ResponseEntity.ok(paymentService.getAvailableBanks());
    }

    @PostMapping
    public ResponseEntity<String> createPayment(
            @RequestBody PaymentRequestDTO request,
            Authentication authentication) {

        CustomUserDetails userDetails =(CustomUserDetails) authentication.getPrincipal();
        String html = paymentService.createPayment(request,userDetails.getUsername());
        return ResponseEntity.ok()
                .contentType(MediaType.TEXT_HTML)
                .body(html);
    }

    @GetMapping("/test/{bookingId}")
    public ResponseEntity<String> testPayment(
            @PathVariable Long bookingId,
            Authentication authentication) {

        CustomUserDetails userDetails =
                (CustomUserDetails) authentication.getPrincipal();

        PaymentRequestDTO request = new PaymentRequestDTO();
        request.setBookingId(bookingId);
        request.setBank("TEST0021");

        String html = paymentService.createPayment(
                request,
                userDetails.getUsername()
        );

        return ResponseEntity.ok()
                .contentType(MediaType.TEXT_HTML)
                .body(html);
    }


    @GetMapping("/status/{bookingId}")
    public ResponseEntity<String> checkPaymentStatus(
            @PathVariable Long bookingId,
            Authentication authentication) {

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        User user = userDetails.getUser();

        return ResponseEntity.ok(
                paymentService.checkPaymentStatus(bookingId, user)
        );
    }

    @PostMapping("/response")
    public ResponseEntity<String> paymentResponse(
            @ModelAttribute GoMobiPaymentResponseDTO response) {

        System.out.println("GoMobi Payment Response received:");
        System.out.println("sellerOrderNo = " + response.getFpx_sellerOrderNo());
        System.out.println("transactionId = " + response.getFpx_fpxTxnId());
        System.out.println("amount = " + response.getFpx_txnAmount());
        System.out.println("debitAuthCode = " + response.getFpx_debitAuthCode());
        System.out.println("debitAuthCodeString = " + response.getFpx_debitAuthCodeString());

        paymentService.processPaymentResponse(response);
        return ResponseEntity.ok("Payment response received");
    }

    @GetMapping("/ipn")
    public ResponseEntity<Map<String, Object>> receiveIPN(
            @RequestParam String callBack) {

        try {
            GoMobiIPNResponseDTO ipn =
                    objectMapper.readValue(callBack, GoMobiIPNResponseDTO.class);

            System.out.println("AUTH CODE RECEIVED = [" + ipn.getFpx_debitAuthCode() + "]");

            System.out.println("GoMobi IPN received:");
            System.out.println("Transaction ID = " + ipn.getFpx_fpxTxnId());
            System.out.println("Order = " + ipn.getFpx_sellerOrderNo());
            System.out.println("Amount = " + ipn.getFpx_txnAmount());
            System.out.println("Code = " + ipn.getFpx_debitAuthCode());
            System.out.println("Description = " + ipn.getFpx_debitAuthCodeString());
            System.out.println("Checksum = " + ipn.getFpx_checkSum());

            if (!"00".equals(ipn.getFpx_debitAuthCode())
                    || !"Approved".equalsIgnoreCase(ipn.getFpx_debitAuthCodeString())) {

                Map<String, Object> response = new HashMap<>();
                response.put("responseCode", 400);
                response.put("responseMessage", "Payment failed");

                return ResponseEntity.badRequest().body(response);
            }

            Map<String, Object> response = new HashMap<>();
            response.put("responseCode", 200);
            response.put("responseMessage", "Successful");

            return ResponseEntity.ok(response);

        } catch (Exception e) {

            Map<String, Object> response = new HashMap<>();
            response.put("responseCode", 500);
            response.put("responseMessage", "Invalid callback");

            return ResponseEntity.badRequest().body(response);
        }
    }
}