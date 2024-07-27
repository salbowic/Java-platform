package praca_inz_proj;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.Base64;

public class Student {
	/* Fields from database */

	private int Nr_studenta;
	private String Nr_indeksu;
	private String Imie;
	private String Nazwisko;
	private String Plec;
	private String Email;
	private String Nr_telefonu;
	private Blob Zdjecie;
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
			String nr_telefonu, Blob zdjecie) {
		super();
		this.Nr_studenta = nr_studenta;
		this.Nr_indeksu = nr_indeksu;
		this.Imie = imie;
		this.Nazwisko = nazwisko;
		this.Plec = plec;
		this.Email = email;
		this.Nr_telefonu = nr_telefonu;
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
		this.Zdjecie = zdjecie;
	}
	
	public String getZdjecie64() {
		return Zdjecie64;
	}

	public void setZdjecie64(String zdjecie64) {
		Zdjecie64 = zdjecie64;
	}

	@Override
	public String toString() {
		return "Student [Nr_studenta=" + Nr_studenta + ", Nr_indeksu=" + Nr_indeksu + ", Imie=" + Imie + ", Nazwisko="
				+ Nazwisko + ", Plec=" + Plec + ", Email=" + Email + ", Nr_telefonu=" + Nr_telefonu + ", Zdjecie="
				+ Zdjecie + "]";
	}
	

}
