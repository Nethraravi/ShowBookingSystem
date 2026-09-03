package org.company.showbookingapp.payment.gomobi;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class GoMobiIPNSignatureUtil {

    public static String generateSignature(
            String transactionId,
            String transactionOrder,
            String transactionDate,
            String transactionAmount,
            String transactionDescription,
            String transactionType,String secretKey) {

        try {
            String input =
                    transactionId + "|" +
                            transactionOrder + "|" +
                            transactionDate + "|" +
                            transactionAmount + "|" +
                            transactionDescription + "|" +
                            transactionType;

            byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);

            SecretKeySpec keySpec =
                    new SecretKeySpec(keyBytes, "DESede");

            Cipher cipher =
                    Cipher.getInstance("DESede/ECB/PKCS5Padding");

            cipher.init(Cipher.ENCRYPT_MODE, keySpec);

            byte[] encrypted =
                    cipher.doFinal(input.getBytes(StandardCharsets.UTF_8));

            return Base64.getEncoder().encodeToString(encrypted);

        } catch (Exception e) {
            throw new RuntimeException(
                    "Failed to generate GoMobi IPN signature", e);
        }
    }

    private GoMobiIPNSignatureUtil() {
    }
}