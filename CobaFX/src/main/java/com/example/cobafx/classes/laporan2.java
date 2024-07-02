package com.example.cobafx.classes;


public class laporan2 {
    Integer count;
    String jenisKelas;

    public laporan2 (String jenisKelas, Integer count) {
        this.count = count;
        this.jenisKelas = jenisKelas;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getJenisKelas() {
        return jenisKelas;
    }

    public void setJenisKelas(String jenisKelas) {
        this.jenisKelas = jenisKelas;
    }
}
