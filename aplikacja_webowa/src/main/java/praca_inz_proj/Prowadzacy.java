package praca_inz_proj;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.Base64;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Prowadzacy {
	/* Fields from database */
	@JsonProperty("Nr_prowadzacego")
	private int Nr_prowadzacego;
	@JsonProperty("Imie")
	private String Imie;
	@JsonProperty("Nazwisko")
	private String Nazwisko;
	@JsonProperty("Plec")
	private String Plec;
	@JsonProperty("Tytul")
	private String Tytul;
	@JsonProperty("Email")
	private String Email;
	@JsonProperty("Nr_telefonu")
	private String Nr_telefonu;
	@JsonProperty("Haslo")
	private String Haslo;
	private Blob Zdjecie;
	private String Zdjecie64;
	

	
	public Prowadzacy() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	/* Constructor */
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
		this.Zdjecie64 = convertBlobToBase64(zdjecie);
	}

	private String convertBlobToBase64(Blob blob) {
		if (blob == null) {
			return null;
		}

		try (InputStream inputStream = blob.getBinaryStream()) {
			byte[] bytes = inputStream.readAllBytes();
			String base64Image = Base64.getEncoder().encodeToString(bytes);
			return base64Image;
		} catch (SQLException | IOException e) {
			e.printStackTrace();
			return "";
		}
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
		return "Prowadzacy [Nr_prowadzacego=" + Nr_prowadzacego + ", Imie=" + Imie + ", Nazwisko=" + Nazwisko
				+ ", Plec=" + Plec + ", Tytul=" + Tytul + ", Email=" + Email + ", Nr_telefonu=" + Nr_telefonu
				+ ", Haslo=" + Haslo + ", Zdjecie=" + Zdjecie + "]";
	}

	public String getFullTytul() {
	    switch (this.Tytul) {
	        case "M": return "Mag. in¿.";
	        case "D": return "Dr in¿.";
	        case "H": return "Dr hab. in¿.";
	        case "N": return "Prof. dr hab. in¿.";
	        case "Z": return "Prof. zw. dr hab. in¿.";
	        default: return "";
	    }
	}
	
}
