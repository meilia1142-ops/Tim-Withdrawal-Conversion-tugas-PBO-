package service;

import dao.LogVendorDAO;
import dao.NasabahDAO;
import dao.PenarikanDAO;
import dao.RekeningDAO;
import model.LogVendor;
import model.Nasabah;
import model.Rekening;
import model.TransaksiPenarikan;
import model.VoucherDigital;
import model.PenarikanTunai;
import model.PenarikanPulsa;
import model.PenarikanToken;
import model.PenarikanSembako;
import model.PenarikanBank;
import model.PenarikanEWallet;
import util.VoucherGenerator;

import java.math.BigDecimal;
import java.util.List;

public class WithdrawalService {

    private final NasabahDAO nasabahDAO = new NasabahDAO();
    private final RekeningDAO rekeningDAO = new RekeningDAO();
    private final PenarikanDAO penarikanDAO = new PenarikanDAO();
    private final PinService pinService = new PinService();
    private final VoucherService voucherService = new VoucherService();

    private Nasabah cariNasabahDanValidasiPin(String pin) {
        List<Nasabah> semuaNasabah = nasabahDAO.findAll();
        if (semuaNasabah == null || semuaNasabah.isEmpty()) {
            throw new RuntimeException("Tidak ada data nasabah di sistem");
        }

        for (Nasabah n : semuaNasabah) {
            if (pinService.verifyPin(pin, n.getPinHash())) {
                return n;
            }
        }
        throw new RuntimeException("PIN salah atau Nasabah tidak ditemukan");
    }

    private Rekening potongSaldo(int idNasabah, BigDecimal nominal) {
        Rekening rekening = rekeningDAO.findByNasabahId(idNasabah);
        if (rekening == null) {
            throw new RuntimeException("Rekening tidak ditemukan");
        }

        if (rekening.getSaldo().compareTo(nominal) < 0) {
            throw new RuntimeException("Saldo tidak mencukupi");
        }

        BigDecimal saldoBaru = rekening.getSaldo().subtract(nominal);
        rekeningDAO.updateSaldo(idNasabah, saldoBaru);
        return rekening;
    }

    private TransaksiPenarikan catatTransaksi(int idNasabah, Integer idVoucher, String jenis, BigDecimal nominal, String status) {
        TransaksiPenarikan trx = new TransaksiPenarikan();
        trx.setIdNasabah(idNasabah);
        trx.setIdVoucher(idVoucher);
        trx.setJenisPenarikan(jenis);
        trx.setNominal(nominal);
        trx.setStatus(status);
        return penarikanDAO.save(trx);
    }

    private void catatLogVendor(int idPenarikan, String vendor, String request, String response, String status) {
        LogVendor log = new LogVendor();
        log.setIdPenarikan(idPenarikan);
        log.setVendor(vendor);
        log.setRequestData(request);
        log.setResponseData(response);
        log.setStatus(status);
        new LogVendorDAO().save(log);
    }

    public boolean tarikTunai(BigDecimal nominal, String pin) {
        try {
            Nasabah nasabah = cariNasabahDanValidasiPin(pin);
            potongSaldo(nasabah.getIdNasabah(), nominal);

            TransaksiPenarikan savedTrx = catatTransaksi(nasabah.getIdNasabah(), null, "TUNAI", nominal, "SUCCESS");

            PenarikanTunai pTunai = new PenarikanTunai(nasabah.getIdNasabah(), nominal);
            pTunai.eksekusiPenarikan();

            catatLogVendor(savedTrx.getIdPenarikan(), "SIMULATOR_TUNAI", "nominal=" + nominal, "CASH_DISPENSED", "SUCCESS");

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean konversiPulsa(BigDecimal nominal, String pin) {
        try {
            Nasabah nasabah = cariNasabahDanValidasiPin(pin);
            potongSaldo(nasabah.getIdNasabah(), nominal);

            VoucherDigital voucher = voucherService.generateVoucher("PULSA", nominal);
            TransaksiPenarikan savedTrx = catatTransaksi(nasabah.getIdNasabah(), voucher.getIdVoucher(), "PULSA", nominal, "SUCCESS");

            PenarikanPulsa pPulsa = new PenarikanPulsa(nasabah.getIdNasabah(), nominal);
            pPulsa.eksekusiPenarikan();

            catatLogVendor(savedTrx.getIdPenarikan(), "SIMULATOR_PULSA", "nominal=" + nominal + "&provider=ALL", "PULSA_TERKIRIM", "SUCCESS");

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public String konversiToken(BigDecimal nominal, String pin) {
        try {
            Nasabah nasabah = cariNasabahDanValidasiPin(pin);
            potongSaldo(nasabah.getIdNasabah(), nominal);

            VoucherDigital voucher = voucherService.generateVoucher("TOKEN", nominal);
            TransaksiPenarikan savedTrx = catatTransaksi(nasabah.getIdNasabah(), voucher.getIdVoucher(), "TOKEN", nominal, "SUCCESS");

            PenarikanToken pToken = new PenarikanToken(nasabah.getIdNasabah(), nominal);
            pToken.eksekusiPenarikan();

            catatLogVendor(savedTrx.getIdPenarikan(), "SIMULATOR_PLN", "nominal=" + nominal, "TOKEN_CODE:" + voucher.getKodeVoucher(), "SUCCESS");

            return voucher.getKodeVoucher();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean konversiSembako(BigDecimal nominal, String pin) {
        try {
            Nasabah nasabah = cariNasabahDanValidasiPin(pin);
            potongSaldo(nasabah.getIdNasabah(), nominal);

            TransaksiPenarikan savedTrx = catatTransaksi(nasabah.getIdNasabah(), null, "SEMBAKO", nominal, "SUCCESS");

            PenarikanSembako pSembako = new PenarikanSembako(nasabah.getIdNasabah(), nominal);
            pSembako.eksekusiPenarikan();

            catatLogVendor(savedTrx.getIdPenarikan(), "SIMULATOR_SEMBAKO", "nominal=" + nominal, "PAKET_SEMBAKO", "SUCCESS");

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean tarikViaBank(BigDecimal nominal, String pin, String namaBank, String noRekening) {
        try {
            Nasabah nasabah = cariNasabahDanValidasiPin(pin);
            potongSaldo(nasabah.getIdNasabah(), nominal);

            TransaksiPenarikan savedTrx = catatTransaksi(nasabah.getIdNasabah(), null, "BANK", nominal, "SUCCESS");

            PenarikanBank pBank = new PenarikanBank(nasabah.getIdNasabah(), nominal, namaBank, noRekening);
            pBank.eksekusiPenarikan();

            catatLogVendor(savedTrx.getIdPenarikan(), "SIMULATOR_BANK_" + namaBank.toUpperCase(), "nominal=" + nominal + "&target=" + noRekening, "TRANSFER_SUCCESS_REF_" + VoucherGenerator.generateVoucher(), "SUCCESS");

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean tarikViaEWallet(BigDecimal nominal, String pin, String namaWallet, String noHp) {
        try {
            Nasabah nasabah = cariNasabahDanValidasiPin(pin);
            potongSaldo(nasabah.getIdNasabah(), nominal);

            TransaksiPenarikan savedTrx = catatTransaksi(nasabah.getIdNasabah(), null, "E_WALLET", nominal, "SUCCESS");

            PenarikanEWallet pWallet = new PenarikanEWallet(nasabah.getIdNasabah(), nominal, namaWallet, noHp);
            pWallet.eksekusiPenarikan();

            catatLogVendor(savedTrx.getIdPenarikan(), "SIMULATOR_EWALLET_" + namaWallet.toUpperCase(), "nominal=" + nominal + "&phone=" + noHp, "TOPUP_SUCCESS_TRX_" + VoucherGenerator.generateVoucher(), "SUCCESS");

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}