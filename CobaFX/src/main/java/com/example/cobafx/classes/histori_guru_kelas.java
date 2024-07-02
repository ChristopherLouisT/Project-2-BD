package com.example.cobafx.classes;

public class histori_guru_kelas {
    Integer id_histori;
    Integer id_guru;
    Integer id_kelastahun;
    public static int lastID = 1;

    public histori_guru_kelas(Integer id_histori, Integer id_anak, Integer id_kelastahun) {
        this.id_histori = id_histori;
        this.id_guru = id_anak;
        this.id_kelastahun = id_kelastahun;
        lastID++;
    }

    public Integer getId_histori() {
        return id_histori;
    }

    public void setId_histori(Integer id_histori) {
        this.id_histori = id_histori;
    }

    public Integer getId_guru() {
        return id_guru;
    }

    public void setId_guru(Integer id_guru) {
        this.id_guru = id_guru;
    }

    public Integer getId_kelastahun() {
        return id_kelastahun;
    }

    public void setId_kelastahun(Integer id_kelastahun) {
        this.id_kelastahun = id_kelastahun;
    }

}
