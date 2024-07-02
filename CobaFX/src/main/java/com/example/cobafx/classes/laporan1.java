package com.example.cobafx.classes;


public class laporan1 {
    String nama;
    Integer count;
    String jenisKelas;

    public laporan1 (Integer count, String nama, String jenisKelas) {
        this.nama = nama;
        this.count = count;
        this.jenisKelas = jenisKelas;
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
