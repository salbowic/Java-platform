package com.example.konsultacjeplusv6.model;

import com.google.gson.annotations.SerializedName;
import android.os.Parcel;
import android.os.Parcelable;

import java.sql.Blob;

public class Student implements Parcelable {
    /* Fields from database */
    @SerializedName("nr_studenta")
    private int Nr_studenta;

    @SerializedName("nr_indeksu")
    private String Nr_indeksu;

    @SerializedName("imie")
    private String Imie;

    @SerializedName("nazwisko")
    private String Nazwisko;

    @SerializedName("plec")
    private String Plec;

    @SerializedName("email")
    private String Email;

    @SerializedName("nr_telefonu")
    private String Nr_telefonu;

    @SerializedName("zdjecie")
    private Blob Zdjecie;
    @SerializedName("zdjecie64")
    private String Zdjecie64;

    /**
     *
     */
    public Student() {
        super();
        // TODO Auto-generated constructor stub
    }

    /* Constructor */

    public Student(int nr_studenta, String nr_indeksu, String imie, String nazwisko, String plec, String email,
                   String nr_telefonu, Blob zdjecie, String zdjecie64) {
        super();
        this.Nr_studenta = nr_studenta;
        this.Nr_indeksu = nr_indeksu;
        this.Imie = imie;
        this.Nazwisko = nazwisko;
        this.Plec = plec;
        this.Email = email;
        this.Nr_telefonu = nr_telefonu;
        this.Zdjecie = zdjecie;
        this.Zdjecie64 = zdjecie64;
    }

    /* Getters and setters */

    public int getNr_studenta() {
        return Nr_studenta;
    }

    public void setNr_studenta(int nr_studenta) {
        this.Nr_studenta = nr_studenta;
    }

    public String getNr_indeksu() {
        return Nr_indeksu;
    }

    public void setNr_indeksu(String nr_indeksu) {
        this.Nr_indeksu = nr_indeksu;
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

    /* toString() */

    @Override
    public String toString() {
        return "Student [Nr_studenta=" + Nr_studenta + ", Nr_indeksu=" + Nr_indeksu + ", Imie=" + Imie + ", Nazwisko="
                + Nazwisko + ", Plec=" + Plec + ", Email=" + Email + ", Nr_telefonu=" + Nr_telefonu + "]";
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(Nr_studenta);
        dest.writeString(Nr_indeksu);
        dest.writeString(Imie);
        dest.writeString(Nazwisko);
        dest.writeString(Plec);
        dest.writeString(Email);
        dest.writeString(Nr_telefonu);
        dest.writeString(Zdjecie64);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<Student> CREATOR = new Parcelable.Creator<Student>() {
        public Student createFromParcel(Parcel in) {
            return new Student(in);
        }

        public Student[] newArray(int size) {
            return new Student[size];
        }
    };

    private Student(Parcel in) {
        Nr_studenta = in.readInt();
        Nr_indeksu = in.readString();
        Imie = in.readString();
        Nazwisko = in.readString();
        Plec = in.readString();
        Email = in.readString();
        Nr_telefonu = in.readString();
        Zdjecie64 = in.readString();
    }

}

