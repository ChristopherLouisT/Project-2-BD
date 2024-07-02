package com.example.cobafx.classes;

public class Guru {
    private int id_guru;
    private String nama;
    private String gender;
    private String tanggal_lahir;
    private String noTelp_guru;
    private String alamat;
    private static int serial = 1;

    public Guru(String nama, String gender, String tanggal_lahir, String noTelp_guru, String alamat) {
        this.id_guru = serial++;
        this.nama = nama;
        this.gender = gender;
        this.tanggal_lahir = tanggal_lahir;
        this.noTelp_guru = noTelp_guru;
        this.alamat = alamat;
    }
    public Guru(Integer id_guru,String nama, String gender, String tanggal_lahir, String noTelp_guru, String alamat) {
        this.id_guru = id_guru;
        this.nama = nama;
        this.gender = gender;
        this.tanggal_lahir = tanggal_lahir;
        this.noTelp_guru = noTelp_guru;
        this.alamat = alamat;
        serial++;
    }

    public int getId_guru() {
        return id_guru;
    }

    public void setId_guru(int id_guru) {
        this.id_guru = id_guru;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getTanggal_lahir() {
        return tanggal_lahir;
    }

    public void setTanggal_lahir(String tanggal_lahir) {
        this.tanggal_lahir = tanggal_lahir;
    }

    public String getNoTelp_guru() {
        return noTelp_guru;
    }

    public void setNoTelp_guru(String noTelp_guru) {
        this.noTelp_guru = noTelp_guru;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public static int getSerial() {
        return serial;
    }

    public static void setSerial(int serial) {
        Guru.serial = serial;
    }
}
