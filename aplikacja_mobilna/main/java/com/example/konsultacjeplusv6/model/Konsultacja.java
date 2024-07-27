package com.example.konsultacjeplusv6.model;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.Nullable;

import java.util.Date;

public class Konsultacja implements Parcelable {
    private int Nr_konsultacji;
    private Date DataOd;
    private Date DataDo;
    @Nullable
    private Integer Nr_pokoju;
    private String Czy_online;
    private String Czy_publiczne;
    @Nullable
    private String Pytanie;
    @Nullable
    private Integer Nr_realizacji;
    private int Nr_prowadzacego;
    private String formattedDataOd;
    private String formattedDataDo;

    public Konsultacja() {
        super();
    }

    public Konsultacja(int nr_konsultacji, Date dataOd, Date dataDo, @Nullable Integer nr_pokoju, String czy_online, String czy_publiczne, @Nullable String pytanie, @Nullable Integer nr_realizacji, int nr_prowadzacego, String formattedDataOd, String formattedDataDo) {
        this.Nr_konsultacji = nr_konsultacji;
        DataOd = dataOd;
        DataDo = dataDo;
        Nr_pokoju = nr_pokoju;
        Czy_online = czy_online;
        Czy_publiczne = czy_publiczne;
        Pytanie = pytanie;
        Nr_realizacji = nr_realizacji;
        Nr_prowadzacego = nr_prowadzacego;
        this.formattedDataOd = formattedDataOd;
        this.formattedDataDo = formattedDataDo;
    }

    protected Konsultacja(Parcel in) {
        Nr_konsultacji = in.readInt();
        DataOd = new Date(in.readLong());
        DataDo = new Date(in.readLong());
        if (in.readByte() == 0) {
            Nr_pokoju = null;
        } else {
            Nr_pokoju = in.readInt();
        }
        Czy_online = in.readString();
        Czy_publiczne = in.readString();
        Pytanie = in.readString();
        if (in.readByte() == 0) {
            Nr_realizacji = null;
        } else {
            Nr_realizacji = in.readInt();
        }
        Nr_prowadzacego = in.readInt();
        formattedDataOd = in.readString();
        formattedDataDo = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(Nr_konsultacji);
        dest.writeLong(DataOd.getTime());
        dest.writeLong(DataDo.getTime());
        if (Nr_pokoju == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(Nr_pokoju);
        }
        dest.writeString(Czy_online);
        dest.writeString(Czy_publiczne);
        dest.writeString(Pytanie);
        if (Nr_realizacji == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(Nr_realizacji);
        }
        dest.writeInt(Nr_prowadzacego);
        dest.writeString(formattedDataOd);
        dest.writeString(formattedDataDo);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Konsultacja> CREATOR = new Creator<Konsultacja>() {
        @Override
        public Konsultacja createFromParcel(Parcel in) {
            return new Konsultacja(in);
        }

        @Override
        public Konsultacja[] newArray(int size) {
            return new Konsultacja[size];
        }
    };

    public int getNr_konsultacji() {
        return Nr_konsultacji;
    }

    public void setNr_konsultacji(int nr_konsultacji) {
        this.Nr_konsultacji = nr_konsultacji;
    }

    public java.util.Date getDataOd() {
        return DataOd;
    }

    public void setDataOd(java.util.Date dataOd) {
        this.DataOd = dataOd;
    }

    public java.util.Date getDataDo() {
        return DataDo;
    }

    public void setDataDo(java.util.Date dataDo) {
        this.DataDo = dataDo;
    }

    public Integer getNr_pokoju() {
        return Nr_pokoju;
    }

    public void setNr_pokoju(Integer nr_pokoju) {
        this.Nr_pokoju = nr_pokoju;
    }

    public String getCzy_online() {
        return Czy_online;
    }

    public void setCzy_online(String czy_online) {
        this.Czy_online = czy_online;
    }

    public String getCzy_publiczne() {
        return Czy_publiczne;
    }

    public void setCzy_publiczne(String czy_publiczne) {
        this.Czy_publiczne = czy_publiczne;
    }

    public String getPytanie() {
        return Pytanie;
    }

    public void setPytanie(String pytanie) {
        this.Pytanie = pytanie;
    }

    public Integer getNr_realizacji() {
        return Nr_realizacji;
    }

    public void setNr_realizacji(Integer nr_realizacji) {
        Nr_realizacji = nr_realizacji;
    }

    public int getNr_prowadzacego() {
        return Nr_prowadzacego;
    }

    public void setNr_prowadzacego(int nr_prowadzacego) {
        Nr_prowadzacego = nr_prowadzacego;
    }

    public String getFormattedDataOd() {
        return formattedDataOd;
    }

    public void setFormattedDataOd(String formattedDataOd) {
        this.formattedDataOd = formattedDataOd;
    }

    public String getFormattedDataDo() {
        return formattedDataDo;
    }

    public void setFormattedDataDo(String formattedDataDo) {
        this.formattedDataDo = formattedDataDo;
    }

    @Override
    public String toString() {
        return "Konsultacja [nr_konsultacji=" + Nr_konsultacji + ", DataOd=" + DataOd + ", DataDo=" + DataDo
                + ", Nr_pokoju=" + Nr_pokoju + ", Czy_online=" + Czy_online + ", Czy_publiczne=" + Czy_publiczne
                + ", Pytanie=" + Pytanie + ", Nr_realizacji=" + Nr_realizacji + ", Nr_prowadzacego=" + Nr_prowadzacego
                + "]";
    }

    public String getFullOnline() {
        switch (this.Czy_online) {
            case "T":
                return "Tak";
            case "N":
                return "Nie";
            default:
                return "";
        }
    }

    public String getFullPubliczne() {
        switch (this.Czy_publiczne) {
            case "T":
                return "Tak";
            case "N":
                return "Nie";
            default:
                return "";
        }
    }
}
