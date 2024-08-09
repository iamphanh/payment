package vn.vnpay.demo1.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class CheckSumUtil {
    // dat ten bien co y nghia
    public static String sha256(String inputString) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(inputString.getBytes(StandardCharsets.UTF_8));
            // todo: phan biet string va stringbuilder, stringbuffer
            StringBuilder hexString = new StringBuilder();

            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
