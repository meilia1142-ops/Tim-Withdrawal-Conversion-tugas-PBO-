package service;

import dao.VoucherDAO;
import model.VoucherDigital;

import java.math.BigDecimal;
import java.util.UUID;

public class VoucherService {

    private final VoucherDAO voucherDAO =
            new VoucherDAO();

    public VoucherDigital generateVoucher(
            String jenisVoucher,
            BigDecimal nominal
    ) {

        VoucherDigital voucher =
                new VoucherDigital();

        voucher.setJenisVoucher(
                jenisVoucher
        );

        voucher.setNominal(
                nominal
        );

        voucher.setStatus(
                "ACTIVE"
        );

        voucher.setKodeVoucher(
                generateKodeVoucher()
        );

        return voucherDAO.save(
                voucher
        );
    }

    private String generateKodeVoucher() {

        return UUID.randomUUID()
                .toString()
                .replace("-", "")
                .substring(0, 12)
                .toUpperCase();

    }
}