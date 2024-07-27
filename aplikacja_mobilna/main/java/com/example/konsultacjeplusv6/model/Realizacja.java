package com.example.konsultacjeplusv6.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Realizacja implements Parcelable {

    @SerializedName("nr_realizacji")
    private int Nr_realizacji;
    @SerializedName("semestr")
    private String Semestr;
    @SerializedName("nr_przedmiotu")
    private int Nr_przedmiotu;

    public Realizacja() {

    }

    public Realizacja(int nr_realizacji, String semestr, int nr_przedmiotu) {
        Nr_realizacji = nr_realizacji;
        Semestr = semestr;
        Nr_przedmiotu = nr_przedmiotu;
    }

    protected Realizacja(Parcel in) {
        Nr_realizacji = in.readInt();
        Semestr = in.readString();
        Nr_przedmiotu = in.readInt();
    }

    public static final Creator<Realizacja> CREATOR = new Creator<Realizacja>() {
        @Override
        public Realizacja createFromParcel(Parcel in) {
            return new Realizacja(in);
        }

        @Override
        public Realizacja[] newArray(int size) {
            return new Realizacja[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(Nr_realizacji);
        dest.writeString(Semestr);
        dest.writeInt(Nr_przedmiotu);
    }

    public int getNr_realizacji() {
        return Nr_realizacji;
    }

    public void setNr_realizacji(int nr_realizacji) {
        Nr_realizacji = nr_realizacji;
    }

    public String getSemestr() {
        return Semestr;
    }

    public void setSemestr(String semestr) {
        Semestr = semestr;
    }

    public int getNr_przedmiotu() {
        return Nr_przedmiotu;
    }

    public void setNr_przedmiotu(int nr_przedmiotu) {
        Nr_przedmiotu = nr_przedmiotu;
    }

    @Override
    public String toString() {
        return "Realizacja [Nr_realizacji=" + Nr_realizacji + ", Semestr=" + Semestr + ", Nr_przedmiotu="
                + Nr_przedmiotu + "]";
    }
}
