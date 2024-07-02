package com.example.cobafx.classes;

public class Anak {
    private int id_anak;
    private String nama;
    private char gender;
    private String tanggal_lahir;
    private String alamat;
    private String nama_wali;
    private String noTelp_wali;
    public static int serial = 1;

    public Anak(Integer id_anak,String nama, char gender, String tanggal_lahir, String alamat, String nama_wali, String noTelp_wali) {
        this.id_anak = id_anak;
        this.nama = nama;
        this.gender = gender;
        this.tanggal_lahir = tanggal_lahir;
//        this.jumlah_kehadiran = jumlah_kehadiran;
        this.alamat = alamat;
        this.nama_wali = nama_wali;
        this.noTelp_wali = noTelp_wali;
        serial++;
    }
//    public Anak(String nama, char gender, String tanggal_lahir, String alamat, String nama_wali, String noTelp_wali) {
//        this.id_anak = ++serial;
//        this.nama = nama;
//        this.gender = gender;
//        this.tanggal_lahir = tanggal_lahir;
////        this.jumlah_kehadiran = jumlah_kehadiran;
//        this.alamat = alamat;
//        this.nama_wali = nama_wali;
//        this.noTelp_wali = noTelp_wali;
//    }

    public static int getSerial() {
        return serial;
    }

    public static void setSerial(int serial) {
        Anak.serial = serial;
    }

    public int getId_anak() {
        return id_anak;
    }

    public void setId_anak(int id_anak) {
        this.id_anak = id_anak;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public char getGender() {
        return gender;
    }

    public void setGender(char gender) {
        this.gender = gender;
    }

    public String getTanggal_lahir() {
        return tanggal_lahir;
    }

    public void setTanggal_lahir(String tanggal_lahir) {
        this.tanggal_lahir = tanggal_lahir;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getNama_wali() {
        return nama_wali;
    }

    public void setNama_wali(String nama_wali) {
        this.nama_wali = nama_wali;
    }

    public String getNoTelp_wali() {
        return noTelp_wali;
    }

    public void setNoTelp_wali(String noTelp_wali) {
        this.noTelp_wali = noTelp_wali;
    }
}
