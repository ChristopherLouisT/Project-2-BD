package com.example.cobafx.classes;

public class histori_anak_kelas {
    Integer id_histori;
    Integer id_anak;
    Integer id_kelasTahun;
    public static int lastID = 1;

    public histori_anak_kelas(Integer id_histori, Integer id_anak, Integer id_kelasTahun) {
        this.id_histori = id_histori;
        this.id_anak = id_anak;
        this.id_kelasTahun = id_kelasTahun;
        lastID++;
    }

    public Integer getId_histori() {
        return id_histori;
    }

    public void setId_histori(Integer id_histori) {
        this.id_histori = id_histori;
    }

    public Integer getId_anak() {
        return id_anak;
    }

    public void setId_anak(Integer id_anak) {
        this.id_anak = id_anak;
    }

    public Integer getId_kelasTahun() {
        return id_kelasTahun;
    }

    public void setId_kelasTahun(Integer id_kelasTahun) {
        this.id_kelasTahun = id_kelasTahun;
    }
}
