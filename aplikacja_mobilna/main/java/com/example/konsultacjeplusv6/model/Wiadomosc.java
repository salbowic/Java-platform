package com.example.konsultacjeplusv6.model;

import com.google.gson.annotations.SerializedName;
import android.os.Parcel;
import android.os.Parcelable;

import java.sql.Timestamp;

public class Wiadomosc implements Parcelable {
        @SerializedName("nr_wiadomosci")
        private int Nr_wiadomosci;
        @SerializedName("nadawca")
        private String Nadawca;
        @SerializedName("tekst")
        private String Tekst;
        @SerializedName("czas")
        private Timestamp Czas;
        @SerializedName("nr_forum")
        private int Nr_forum;
        @SerializedName("nr_prowadzacego")
        private Integer Nr_prowadzacego;
        @SerializedName("nr_studenta")
        private Integer Nr_studenta;
        private String zdjecieNadawcy;

        public Wiadomosc() {
        }

        protected Wiadomosc(Parcel in) {
            Nr_wiadomosci = in.readInt();
            Nadawca = in.readString();
            Tekst = in.readString();
            Czas = (Timestamp) in.readSerializable();
            Nr_forum = in.readInt();
            if (in.readByte() == 0) {
                Nr_prowadzacego = null;
            } else {
                Nr_prowadzacego = in.readInt();
            }
            if (in.readByte() == 0) {
                Nr_studenta = null;
            } else {
                Nr_studenta = in.readInt();
            }
            zdjecieNadawcy = in.readString();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(Nr_wiadomosci);
            dest.writeString(Nadawca);
            dest.writeString(Tekst);
            dest.writeSerializable(Czas);
            dest.writeInt(Nr_forum);
            if (Nr_prowadzacego == null) {
                dest.writeByte((byte) 0);
            } else {
                dest.writeByte((byte) 1);
                dest.writeInt(Nr_prowadzacego);
            }
            if (Nr_studenta == null) {
                dest.writeByte((byte) 0);
            } else {
                dest.writeByte((byte) 1);
                dest.writeInt(Nr_studenta);
            }
            dest.writeString(zdjecieNadawcy);
        }

        public static final Creator<Wiadomosc> CREATOR = new Creator<Wiadomosc>() {
            @Override
            public Wiadomosc createFromParcel(Parcel in) {
                return new Wiadomosc(in);
            }

            @Override
            public Wiadomosc[] newArray(int size) {
                return new Wiadomosc[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

    public Wiadomosc(int nr_wiadomosci, String nadawca, String tekst, Timestamp czas, int nr_forum, Integer nr_prowadzacego,
                     Integer nr_studenta) {
        Nr_wiadomosci = nr_wiadomosci;
        Nadawca = nadawca;
        Tekst = tekst;
        Czas = czas;
        Nr_forum = nr_forum;
        Nr_prowadzacego = nr_prowadzacego;
        Nr_studenta = nr_studenta;
    }


    public int getNr_wiadomosci() {
        return Nr_wiadomosci;
    }

    public void setNr_wiadomosci(int nr_wiadomosci) {
        this.Nr_wiadomosci = nr_wiadomosci;
    }

    public String getNadawca() {
        return Nadawca;
    }

    public void setNadawca(String nadawca) {
        this.Nadawca = nadawca;
    }

    public String getTekst() {
        return Tekst;
    }

    public void setTekst(String tekst) {
        this.Tekst = tekst;
    }

    public Timestamp getCzas() {
        return Czas;
    }

    public void setCzas(Timestamp czas) {
        this.Czas = czas;
    }

    public int getNr_forum() {
        return Nr_forum;
    }

    public void setNr_forum(int nr_forum) {
        this.Nr_forum = nr_forum;
    }

    public Integer getNr_prowadzacego() {
        return Nr_prowadzacego;
    }

    public void setNr_prowadzacego(Integer nr_prowadzacego) {
        Nr_prowadzacego = nr_prowadzacego;
    }

    public Integer getNr_studenta() {
        return Nr_studenta;
    }

    public void setNr_studenta(Integer nr_studenta) {
        Nr_studenta = nr_studenta;
    }

    public String getZdjecieNadawcy() {
        return zdjecieNadawcy;
    }

    public void setZdjecieNadawcy(String zdjecieNadawcy) {
        this.zdjecieNadawcy = zdjecieNadawcy;
    }

    @Override
    public String toString() {
        return "Wiadomosc [Nr_wiadomosci=" + Nr_wiadomosci + ", nadawca=" + Nadawca + ", tekst=" + Tekst + ", czas="
                + Czas + ", Nr_forum=" + Nr_forum + ", Nr_prowadzacego=" + Nr_prowadzacego + ", Nr_studenta="
                + Nr_studenta + "]";
    }

}

