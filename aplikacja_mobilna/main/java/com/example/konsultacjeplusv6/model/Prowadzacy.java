package com.example.konsultacjeplusv6.model;


import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.sql.Blob;

public class Prowadzacy implements Parcelable {
    /* Fields from database */
    @SerializedName("nr_prowadzacego")
    private int Nr_prowadzacego;
    @SerializedName("imie")
    private String Imie;
    @SerializedName("nazwisko")
    private String Nazwisko;
    @SerializedName("plec")
    private String Plec;
    @SerializedName("tytul")
    private String Tytul;
    @SerializedName("email")
    private String Email;
    @SerializedName("nr_telefonu")
    private String Nr_telefonu;
    @SerializedName("haslo")
    private String Haslo;
    @SerializedName("zdjecie")
    private Blob Zdjecie;
    @SerializedName("zdjecie64")
    private String Zdjecie64;


    public Prowadzacy() {
        super();
        // TODO Auto-generated constructor stub
    }

    /* Constructor */

    /**
     * @param nr_prowadzacego
     * @param imie
     * @param nazwisko
     * @param plec
     * @param tytul
     * @param email
     * @param nr_telefonu
     */
    public Prowadzacy(int nr_prowadzacego, String imie, String nazwisko, String plec, String tytul, String email,
                      String nr_telefonu, String haslo, Blob zdjecie, String zdjecie64) {
        super();
        this.Nr_prowadzacego = nr_prowadzacego;
        this.Imie = imie;
        this.Nazwisko = nazwisko;
        this.Plec = plec;
        this.Tytul = tytul;
        this.Email = email;
        this.Nr_telefonu = nr_telefonu;
        this.Haslo = haslo;
        this.Zdjecie = zdjecie;
        this.Zdjecie64 = zdjecie64;
    }


    public int getNr_prowadzacego() {
        return Nr_prowadzacego;
    }

    public void setNr_prowadzacego(int nr_prowadzacego) {
        this.Nr_prowadzacego = nr_prowadzacego;
    }

    public String getImie() {
        return Imie;
    }

    public void setImie(String imie) {
        this.Imie = imie;
    }

    public String getNazwisko() {
        return Nazwisko;
    }

    public void setNazwisko(String nazwisko) {
        this.Nazwisko = nazwisko;
    }

    public String getPlec() {
        return Plec;
    }

    public void setPlec(String plec) {
        this.Plec = plec;
    }

    public String getTytul() {
        return Tytul;
    }

    public void setTytul(String tytul) {
        this.Tytul = tytul;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        this.Email = email;
    }

    public String getNr_telefonu() {
        return Nr_telefonu;
    }

    public void setNr_telefonu(String nr_telefonu) {
        this.Nr_telefonu = nr_telefonu;
    }

    public String getHaslo() {
        return Haslo;
    }

    public void setHaslo(String haslo) {
        Haslo = haslo;
    }

    public Blob getZdjecie() {
        return Zdjecie;
    }

    public void setZdjecie(Blob zdjecie) {
        Zdjecie = zdjecie;
    }

    public String getZdjecie64() {
        return Zdjecie64;
    }

    public void setZdjecie64(String zdjecie64) {
        Zdjecie64 = zdjecie64;
    }

    @Override
    public String toString() {
        return "Prowadzacy{" +
                "Nr_prowadzacego=" + Nr_prowadzacego +
                ", Imie='" + Imie + '\'' +
                ", Nazwisko='" + Nazwisko + '\'' +
                ", Plec='" + Plec + '\'' +
                ", Tytul='" + Tytul + '\'' +
                ", Email='" + Email + '\'' +
                ", Nr_telefonu='" + Nr_telefonu + '\'' +
                ", Haslo='" + Haslo + '\'' +
                '}';
    }

    public String getFullTytul() {
        switch (this.Tytul) {
            case "M":
                return "Mag. inż.";
            case "D":
                return "Dr inż.";
            case "H":
                return "Dr hab. inż.";
            case "N":
                return "Prof. dr hab. inż.";
            case "Z":
                return "Prof. zw. dr hab. inż.";
            default:
                return "";
        }
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(Nr_prowadzacego);
        dest.writeString(Imie);
        dest.writeString(Nazwisko);
        dest.writeString(Plec);
        dest.writeString(Tytul);
        dest.writeString(Email);
        dest.writeString(Nr_telefonu);
        dest.writeString(Haslo);
        dest.writeString(Zdjecie64);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<Prowadzacy> CREATOR = new Parcelable.Creator<Prowadzacy>() {
        public Prowadzacy createFromParcel(Parcel in) {
            return new Prowadzacy(in);
        }

        public Prowadzacy[] newArray(int size) {
            return new Prowadzacy[size];
        }
    };

    private Prowadzacy(Parcel in) {
        Nr_prowadzacego = in.readInt();
        ;
        Imie = in.readString();
        Nazwisko = in.readString();
        Plec = in.readString();
        Tytul = in.readString();
        Email = in.readString();
        Nr_telefonu = in.readString();
        Haslo = in.readString();
        Zdjecie64 = in.readString();
    }

}

