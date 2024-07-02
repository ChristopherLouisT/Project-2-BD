package com.example.cobafx.classes;

public class Kelas {
    private int id_kelas;
    private String jeniskelas;
    public static int lastID = 1;

    public Kelas(int id_kelas, String jeniskelas) {
        this.id_kelas = id_kelas;
        this.jeniskelas = jeniskelas;
        lastID++;
    }

    public int getId_kelas() {
        return id_kelas;
    }

    public void setId_kelas(int id_kelas) {
        this.id_kelas = id_kelas;
    }

    public String getJeniskelas() {
        return jeniskelas;
    }

    public void setJeniskelas(String jenis_kelas) {
        this.jeniskelas = jenis_kelas;
    }
}