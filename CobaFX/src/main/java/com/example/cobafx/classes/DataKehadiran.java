package com.example.cobafx.classes;


import java.sql.Date;

public class DataKehadiran {
    Integer idKehadiran, idAnak;
    String absence;
    Date date;
    public static Integer lastID = 1;

    public DataKehadiran(Integer idKehadiran, Integer idAnak, String absence, Date date) {
        this.idKehadiran = idKehadiran;
        this.idAnak = idAnak;
        this.absence = absence;
        this.date = date;
        lastID++;
    }

    public Integer getIdKehadiran() {
        return idKehadiran;
    }

    public void setIdKehadiran(Integer idKehadiran) {
        this.idKehadiran = idKehadiran;
    }

    public Integer getIdAnak() {
        return idAnak;
    }

    public void setIdAnak(Integer idAnak) {
        this.idAnak = idAnak;
    }

    public String getAbsence() {
        return absence;
    }

    public void setAbsence(String absence) {
        this.absence = absence;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
