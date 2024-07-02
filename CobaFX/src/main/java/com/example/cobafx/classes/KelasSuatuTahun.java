package com.example.cobafx.classes;

public class KelasSuatuTahun{
    private int id_kelastahun;
    private int id_tahunajaran;
    private int id_kelas;
    private String kelas_suatu_tahun;
    public static int serial = 1;

    public KelasSuatuTahun(Integer id_kelastahun,Integer id_tahunajaran,Integer id_kelas,String kelas_suatu_tahun) {
        this.id_kelastahun = id_kelastahun;
        this.id_tahunajaran = id_tahunajaran;
        this.id_kelas = id_kelas;
        this.kelas_suatu_tahun = kelas_suatu_tahun;
        serial++;
    }
//    public KelasSuatuTahun(Integer id_tahunajaran,Integer id_kelas,String kelas_suatu_tahun) {
//        this.id_kelastahun = ++serial;
//        this.id_tahunajaran = id_tahunajaran;
//        this.id_kelas = id_kelas;
//        this.kelas_suatu_tahun = kelas_suatu_tahun;
//    }

    public int getId_kelastahun() {
        return id_kelastahun;
    }

    public void setId_kelastahun(int id_kelastahun) {
        this.id_kelastahun = id_kelastahun;
    }

    public int getId_tahunajaran() {
        return id_tahunajaran;
    }

    public void setId_tahunajaran(int id_tahunajaran) {
        this.id_tahunajaran = id_tahunajaran;
    }

    public int getId_kelas() {
        return id_kelas;
    }

    public void setId_kelas(int id_kelas) {
        this.id_kelas = id_kelas;
    }

    public String getKelas_suatu_tahun() {
        return kelas_suatu_tahun;
    }

    public void setKelas_suatu_tahun(String kelas_suatu_tahun) {
        this.kelas_suatu_tahun = kelas_suatu_tahun;
    }

    public static int getSerial() {
        return serial;
    }

    public static void setSerial(int serial) {
        KelasSuatuTahun.serial = serial;
    }
}