package com.example.sholatki.models;

public class Hadith {
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private int id;
    private String perawi;
    private String number;
    private String arabicHadith;


    private String indonesianHadith;

    public String getPerawi() {
        return perawi;
    }

    public void setPerawi(String perawi) {
        this.perawi = perawi;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getArabicHadith() {
        return arabicHadith;
    }

    public void setArabicHadith(String arabicHadith) {
        this.arabicHadith = arabicHadith;
    }

    public String getIndonesianHadith() {
        return indonesianHadith;
    }

    public void setIndonesianHadith(String indonesianHadith) {
        this.indonesianHadith = indonesianHadith;
    }
}
