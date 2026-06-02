INSERT INTO tb_nasabah
(nama, no_hp, pin, status)
VALUES
('nina', '081234567890', '123456', 'AKTIF'),
('meli', '081234567891', '123456', 'AKTIF'),
('anwar', '081234567893', '123456', 'AKTIF'),
('dewa', '081234567892', '123456', 'AKTIF')
;

SELECT * FROM tb_nasabah; 

INSERT INTO tb_rekening
(id_nasabah, saldo)
VALUES
(1, 500000),
(2, 250000),
(3, 100000),
(4, 9000000);

SELECT * FROM tb_rekening;

INSERT INTO tb_voucher_digital
(kode_voucher, jenis_voucher, nominal, status)
VALUES
('PLS10000A', 'PULSA', 10000, 'TERSEDIA'),
('PLS25000A', 'PULSA', 25000, 'TERSEDIA'),

('TKN20000A', 'TOKEN', 20000, 'TERSEDIA'),
('TKN50000A', 'TOKEN', 50000, 'TERSEDIA'),

('SMB50000A', 'SEMBAKO', 50000, 'TERSEDIA'),
('SMB100000A', 'SEMBAKO', 100000, 'TERSEDIA');

SELECT * FROM tb_voucher_digital;

SELECT
n.id_nasabah,
n.nama,
r.saldo
FROM tb_nasabah n
JOIN tb_rekening r
ON n.id_nasabah = r.id_nasabah;
