package com.example.cobafx.classes;

public class laporan3 {
    String nama;
    String jenisKelas;
    Integer count;

    public laporan3 (Integer count, String nama, String jenisKelas) {
        this.nama = nama;
        this.jenisKelas = jenisKelas;
        this.count = count;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getJenisKelas() {
        return jenisKelas;
    }

    public void setJenisKelas(String jenisKelas) {
        this.jenisKelas = jenisKelas;
    }
}
