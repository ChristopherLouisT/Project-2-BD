package com.example.cobafx.classes;

public class laporan4 {
    String gender;
    String jenisKelas;
    Integer count;

    public laporan4 (Integer count, String gender, String jenisKelas) {
        this.gender = gender;
        this.jenisKelas = jenisKelas;
        this.count = count;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getJenisKelas() {
        return jenisKelas;
    }

    public void setJenisKelas(String jenisKelas) {
        this.jenisKelas = jenisKelas;
    }
}
