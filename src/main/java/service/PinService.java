package service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class PinService {

    public String hashPin(String pin) {

        try {

            MessageDigest md =
                    MessageDigest.getInstance("SHA-256");

            byte[] hash =
                    md.digest(
                            pin.getBytes(
                                    StandardCharsets.UTF_8
                            )
                    );

            StringBuilder sb =
                    new StringBuilder();

            for (byte b : hash) {

                sb.append(
                        String.format(
                                "%02x",
                                b
                        )
                );

            }

            return sb.toString();

        } catch (Exception e) {

            throw new RuntimeException(e);

        }
    }

    public boolean verifyPin(
            String inputPin,
            String storedHash
    ) {

        String hashedInput =
                hashPin(inputPin);

        System.out.println(
                "INPUT PIN     : " + inputPin
        );

        System.out.println(
                "HASH INPUT    : " + hashedInput
        );

        System.out.println(
                "HASH DATABASE : " + storedHash
        );

        System.out.println(
                "MATCH ? " +
                        hashedInput.equals(storedHash)
        );

        return hashedInput.equals(storedHash);
    }
}
