package com.example.konsultacjeplusv6.model;

public class Przedmiot {

    /* Fields from server */
    private int Nr_przedmiotu;

    private String Nazwa_przedmiotu;

    private String Skrot_przedmiotu;

    private int ECTS;

    public Przedmiot() {
        super();
    }

    /* Constructor */

    public Przedmiot(int nr_przedmiotu, String nazwa_przedmiotu, String skrot_przedmiotu, int eCTS) {
        super();
        this.Nr_przedmiotu = nr_przedmiotu;
        this.Nazwa_przedmiotu = nazwa_przedmiotu;
        this.Skrot_przedmiotu = skrot_przedmiotu;
        this.ECTS = eCTS;
    }

    public int getNr_przedmiotu() {
        return Nr_przedmiotu;
    }

    public void setNr_przedmiotu(int nr_przedmiotu) {
        Nr_przedmiotu = nr_przedmiotu;
    }

    public String getNazwa_przedmiotu() {
        return Nazwa_przedmiotu;
    }

    public void setNazwa_przedmiotu(String nazwa_przedmiotu) {
        Nazwa_przedmiotu = nazwa_przedmiotu;
    }

    public String getSkrot_przedmiotu() {
        return Skrot_przedmiotu;
    }

    public void setSkrot_przedmiotu(String skrot_przedmiotu) {
        Skrot_przedmiotu = skrot_przedmiotu;
    }

    public int getECTS() {
        return ECTS;
    }

    public void setECTS(int ECTS) {
        this.ECTS = ECTS;
    }

    /* toString() */

    @Override
    public String toString() {
        return "PrzedmiotyEntity{" +
                "Nr_przedmiotu=" + Nr_przedmiotu +
                ", Nazwa_przedmiotu='" + Nazwa_przedmiotu + '\'' +
                ", Skrot_przedmiotu='" + Skrot_przedmiotu + '\'' +
                ", ECTS=" + ECTS +
                '}';
    }
}

