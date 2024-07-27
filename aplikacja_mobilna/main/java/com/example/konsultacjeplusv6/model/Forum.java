package com.example.konsultacjeplusv6.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

public class Forum implements Parcelable {
    /* Fields from database */
    @SerializedName("nr_forum")
    private int Nr_forum;
    @SerializedName("rodzaj")
    private String Rodzaj;
    @Nullable
    @SerializedName("nr_realizacji")
    private Integer Nr_realizacji;
    @Nullable
    @SerializedName("nr_specjalizacji")
    private Integer Nr_specjalizacji;
    /**
     *
     */
    public Forum() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @param nr_forum
     * @param rodzaj
     * @param nr_realizacji
     * @param nr_specjalizacji
     */
    public Forum(int nr_forum, String rodzaj, Integer nr_realizacji, Integer nr_specjalizacji) {
        super();
        Nr_forum = nr_forum;
        Rodzaj = rodzaj;
        Nr_realizacji = nr_realizacji;
        Nr_specjalizacji = nr_specjalizacji;
    }

    /* Getters and setters */
    public int getNr_forum() {
        return Nr_forum;
    }

    public void setNr_forum(int nr_forum) {
        this.Nr_forum = nr_forum;
    }

    public String getRodzaj() {
        return Rodzaj;
    }

    public void setRodzaj(String rodzaj) {
        this.Rodzaj = rodzaj;
    }

    public Integer getNr_realizacji() {
        return Nr_realizacji;
    }

    public void setNr_realizacji(Integer nr_realizacji) {
        Nr_realizacji = nr_realizacji;
    }

    public Integer getNr_specjalizacji() {
        return Nr_specjalizacji;
    }

    public void setNr_specjalizacji(Integer nr_specjalizacji) {
        Nr_specjalizacji = nr_specjalizacji;
    }

    /* toString() */
    @Override
    public String toString() {
        return "Forum [Nr_forum=" + Nr_forum + ", Rodzaj=" + Rodzaj + ", Nr_realizacji=" + Nr_realizacji
                + ", Nr_specjalizacji=" + Nr_specjalizacji + "]";
    }

    public String getFullRodzaj() {
        switch (this.Rodzaj) {
            case "S":
                return "Dla studentów";
            case "P":
                return "Dla prowadzących";
            case "W":
                return "Dla wszystkich";
            default:
                return "";
        }
    }

    // Parcelable
    protected Forum(Parcel in) {
        Nr_forum = in.readInt();
        Rodzaj = in.readString();
        if (in.readByte() == 0) {
            Nr_realizacji = null;
        } else {
            Nr_realizacji = in.readInt();
        }
        if (in.readByte() == 0) {
            Nr_specjalizacji = null;
        } else {
            Nr_specjalizacji = in.readInt();
        }
    }

    public static final Creator<Forum> CREATOR = new Creator<Forum>() {
        @Override
        public Forum createFromParcel(Parcel in) {
            return new Forum(in);
        }

        @Override
        public Forum[] newArray(int size) {
            return new Forum[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(Nr_forum);
        dest.writeString(Rodzaj);
        if (Nr_realizacji == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(Nr_realizacji);
        }
        if (Nr_specjalizacji == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(Nr_specjalizacji);
        }
    }

}
