package util;

import java.util.UUID;

public class VoucherGenerator {

    public static String generateVoucher() {

        return UUID.randomUUID()
                .toString()
                .replace("-", "")
                .substring(0, 12)
                .toUpperCase();
    }
}