package org.company.showbookingapp.notification;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailNotificationService {

    private final JavaMailSender mailSender;

    public void sendPaymentSuccessEmail(
            String to,
            Long bookingId,
            String transactionId,
            String amount) {

        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom("nethraravichandran13@gmail.com");
        message.setTo(to);
        message.setSubject("Payment Successful - Booking Confirmed");

        message.setText(
                "Your payment was successful.\n\n" +
                        "Booking ID: " + bookingId + "\n" +
                        "Transaction ID: " + transactionId + "\n" +
                        "Amount: MYR " + amount + "\n\n" +
                        "Your booking has been confirmed.\n\n" +
                        "Thank you for booking with us."
        );

        mailSender.send(message);
    }

}