package com.example.cobafx.classes;

public class Anak {
    private static int id;
    private String name;
    private char gender;
    private String birthDate;
    private int jumlahKehadiran;
    private String address;
    private String waliName;
    private String noTelpWali;

    public Anak(String name, char gender, String birthDate, String address, String waliName, String noTelpWali) {
        this.name = name;
        this.gender = gender;
        this.birthDate = birthDate;
        this.jumlahKehadiran = 0;
        this.address = address;
        this.waliName = waliName;
        this.noTelpWali = noTelpWali;
    }
}
