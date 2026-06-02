CREATE TABLE tb_nasabah (
    id_nasabah BIGSERIAL PRIMARY KEY,
    nama VARCHAR(100) NOT NULL,
    no_hp VARCHAR(20) UNIQUE,
    pin VARCHAR(255) NOT NULL,
    status VARCHAR(20) DEFAULT 'AKTIF',
    created_at TIMESTAMP DEFAULT NOW()
);

CREATE TABLE tb_rekening (
    id_rekening BIGSERIAL PRIMARY KEY,
    id_nasabah BIGINT UNIQUE NOT NULL,
    saldo NUMERIC(15,2) DEFAULT 0,
    updated_at TIMESTAMP DEFAULT NOW(),
    CONSTRAINT fk_rekening_nasabah
    FOREIGN KEY (id_nasabah)
    REFERENCES tb_nasabah(id_nasabah)
);

CREATE TABLE tb_voucher_digital (
    id_voucher BIGSERIAL PRIMARY KEY,
    kode_voucher VARCHAR(100) UNIQUE NOT NULL,
    jenis_voucher VARCHAR(20) NOT NULL,
    nominal NUMERIC(15,2) NOT NULL,
    status VARCHAR(20) DEFAULT 'TERSEDIA',
    created_at TIMESTAMP DEFAULT NOW()
);

CREATE TABLE tb_penarikan (
    id_penarikan BIGSERIAL PRIMARY KEY,
    id_nasabah BIGINT NOT NULL,
    id_voucher BIGINT,
    jenis_penarikan VARCHAR(20) NOT NULL,
    nominal NUMERIC(15,2) NOT NULL,
    status VARCHAR(20) DEFAULT 'SUCCESS',
    created_at TIMESTAMP DEFAULT NOW(),
    CONSTRAINT fk_penarikan_nasabah
    FOREIGN KEY (id_nasabah)
    REFERENCES tb_nasabah(id_nasabah),
    CONSTRAINT fk_penarikan_voucher
    FOREIGN KEY (id_voucher)
    REFERENCES tb_voucher_digital(id_voucher)
);

CREATE TABLE tb_log_vendor (
    id_log BIGSERIAL PRIMARY KEY,
    id_penarikan BIGINT NOT NULL,
    vendor VARCHAR(100),
    request_data TEXT,
    response_data TEXT,
    status VARCHAR(20),
    created_at TIMESTAMP DEFAULT NOW(),
    CONSTRAINT fk_log_penarikan
    FOREIGN KEY (id_penarikan)
    REFERENCES tb_penarikan(id_penarikan)
);
