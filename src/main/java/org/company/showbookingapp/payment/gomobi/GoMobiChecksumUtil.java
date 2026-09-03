package org.company.showbookingapp.payment.gomobi;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.util.Base64;

public class GoMobiChecksumUtil {

    private static final int ITERATION_COUNT = 65536;
    private static final int KEY_LENGTH = 256;
    private static final int IV_LENGTH = 16;

    private GoMobiChecksumUtil() {
    }

    public static String encryptPayload(
            String input,
            String mid,
            String tid) {

        try {
            // Generate random IV
            SecureRandom secureRandom = new SecureRandom();
            byte[] iv = new byte[IV_LENGTH];
            secureRandom.nextBytes(iv);

            IvParameterSpec ivSpec = new IvParameterSpec(iv);

            // Derive 256-bit AES key using PBKDF2
            SecretKeyFactory factory =
                    SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");

            KeySpec spec = new PBEKeySpec(
                    mid.toCharArray(),
                    tid.getBytes(StandardCharsets.UTF_8),
                    ITERATION_COUNT,
                    KEY_LENGTH
            );

            SecretKeySpec secretKeySpec =
                    new SecretKeySpec(
                            factory.generateSecret(spec).getEncoded(),
                            "AES"
                    );

            // AES/CBC/PKCS5Padding
            Cipher cipher =
                    Cipher.getInstance("AES/CBC/PKCS5Padding");

            cipher.init(
                    Cipher.ENCRYPT_MODE,
                    secretKeySpec,
                    ivSpec
            );

            byte[] cipherText =
                    cipher.doFinal(input.getBytes(StandardCharsets.UTF_8));

            // Combine IV + ciphertext
            byte[] encryptedData =
                    new byte[iv.length + cipherText.length];

            System.arraycopy(
                    iv,
                    0,
                    encryptedData,
                    0,
                    iv.length
            );

            System.arraycopy(
                    cipherText,
                    0,
                    encryptedData,
                    iv.length,
                    cipherText.length
            );

            // Return Base64
            return Base64.getEncoder()
                    .encodeToString(encryptedData);

        } catch (Exception e) {
            throw new RuntimeException("GoMobi AES encryption failed", e);
        }
    }
}