package org.company.showbookingapp.payment.gomobi;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Service
@RequiredArgsConstructor
public class GoMobiChecksumService {

    private final GoMobiProperties goMobiProperties;

    public String generateChecksum(
            String amount,
            String sellerOrderNo,
            String subMID
    ) {

        String message = amount + "|" + sellerOrderNo + "|" + subMID;

        try {
            SecretKeySpec key = new SecretKeySpec(
                    goMobiProperties.getTid().getBytes(StandardCharsets.UTF_8),
                    "AES"
            );

            IvParameterSpec iv = new IvParameterSpec(
                    goMobiProperties.getMid().getBytes(StandardCharsets.UTF_8)
            );

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

            cipher.init(
                    Cipher.ENCRYPT_MODE,
                    key,
                    iv
            );

            byte[] encrypted = cipher.doFinal(
                    message.getBytes(StandardCharsets.UTF_8)
            );

            return Base64.getEncoder().encodeToString(encrypted);

        } catch (Exception e) {
            throw new IllegalStateException(
                    "Failed to generate GoMobi checksum",
                    e
            );
        }
    }

    public String testChecksum() {

        String amount = "5.00";
        String sellerOrderNo = "INV001";

        return generateChecksum(
                amount,
                sellerOrderNo,
                goMobiProperties.getSubMid()
        );
    }
}