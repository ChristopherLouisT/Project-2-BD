package com.example.cobafx.classes;

public class TahunAjaran {
    private int id_tahunajaran;
    private String tahun_ajaran;
    private static int serial = 1;

    public TahunAjaran(int id_tahunajaran, String tahun_ajaran) {
        this.id_tahunajaran = id_tahunajaran;
        this.tahun_ajaran = tahun_ajaran;
        serial++;
    }
    public TahunAjaran(String tahun_ajaran) {
        this.id_tahunajaran = serial++;
        this.tahun_ajaran = tahun_ajaran;
    }

    public int getId_tahunajaran() {
        return id_tahunajaran;
    }

    public void setId_tahunajaran(int id_tahunajaran) {
        this.id_tahunajaran = id_tahunajaran;
    }

    public String getTahun_ajaran() {
        return tahun_ajaran;
    }

    public void setTahun_ajaran(String tahun_ajaran) {
        this.tahun_ajaran = tahun_ajaran;
    }

    public static int getSerial() {
        return serial;
    }

    public static void setSerial(int serial) {
        TahunAjaran.serial = serial;
    }
}

