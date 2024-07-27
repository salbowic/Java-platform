package praca_inz_proj;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Przedmiot {

	/* Fields from database */
	@JsonProperty("Nr_przedmiotu")
	private int Nr_przedmiotu;
	@JsonProperty("Nazwa_przedmiotu")
	private String Nazwa_przedmiotu;
	@JsonProperty("Skrot_przedmiotu")
	private String Skrot_przedmiotu;
	@JsonProperty("ECTS")
	private int ECTS;

	public Przedmiot() {
		super();
		// TODO Auto-generated constructor stub
	}

	/* Constructor */

	public Przedmiot(int nr_przedmiotu, String nazwa_przedmiotu, String skrot_przedmiotu, int eCTS) {
		super();
		this.Nr_przedmiotu = nr_przedmiotu;
		this.Nazwa_przedmiotu = nazwa_przedmiotu;
		this.Skrot_przedmiotu = skrot_przedmiotu;
		this.ECTS = eCTS;
	}

	/* Getters and setters */

	public int getNr_przedmiotu() {
		return Nr_przedmiotu;
	}

	public void setNr_przedmiotu(int nr_przedmiotu) {
		this.Nr_przedmiotu = nr_przedmiotu;
	}

	public String getNazwa_przedmiotu() {
		return Nazwa_przedmiotu;
	}

	public void setNazwa_przedmiotu(String nazwa_przedmiotu) {
		this.Nazwa_przedmiotu = nazwa_przedmiotu;
	}

	public String getSkrot_przedmiotu() {
		return Skrot_przedmiotu;
	}

	public void setSkrot_przedmiotu(String skrot_przedmiotu) {
		this.Skrot_przedmiotu = skrot_przedmiotu;
	}

	public int getECTS() {
		return ECTS;
	}

	public void setECTS(int eCTS) {
		this.ECTS = eCTS;
	}

	/* toString() */

	@Override
	public String toString() {
		return "Przedmiot [Nr_przedmiotu=" + Nr_przedmiotu + ", Nazwa_przedmiotu=" + Nazwa_przedmiotu
				+ ", Skrot_przedmiotu=" + Skrot_przedmiotu + ", ECTS=" + ECTS + "]";
	}

}
