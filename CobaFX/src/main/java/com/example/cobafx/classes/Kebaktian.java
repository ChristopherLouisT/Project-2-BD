package com.example.cobafx.classes;


import java.sql.Date;

public class Kebaktian {
    Integer id;
    String temaKebaktian;
    Date date;
    public static int lastID = 1;

    public Kebaktian (Integer id, String temaKebaktian, Date date) {
        this.id = id;
        this.temaKebaktian = temaKebaktian;
        this.date = date;
        lastID++;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTemaKebaktian() {
        return temaKebaktian;
    }

    public void setTemaKebaktian(String temaKebaktian) {
        this.temaKebaktian = temaKebaktian;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
