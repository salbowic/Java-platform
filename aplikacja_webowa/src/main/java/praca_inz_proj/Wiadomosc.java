package praca_inz_proj;

import java.sql.Timestamp;

public class Wiadomosc {
	private int Nr_wiadomosci;
	private String nadawca;
	private String tekst;
	private Timestamp czas;
	private int Nr_forum;
	private Integer Nr_prowadzacego;
	private Integer Nr_studenta;
	private String zdjecieNadawcy;

	public Wiadomosc() {
	}

	public Wiadomosc(int nr_wiadomosci, String nadawca, String tekst, Timestamp czas, int nr_forum, Integer nr_prowadzacego,
			Integer nr_studenta) {
		this.Nr_wiadomosci = nr_wiadomosci;
		this.nadawca = nadawca;
		this.tekst = tekst;
		this.czas = czas;
		this.Nr_forum = nr_forum;
		this.Nr_prowadzacego = nr_prowadzacego;
		this.Nr_studenta = nr_studenta;
	}

	public int getNr_wiadomosci() {
		return Nr_wiadomosci;
	}

	public void setNr_wiadomosci(int nr_wiadomosci) {
		this.Nr_wiadomosci = nr_wiadomosci;
	}

	public String getNadawca() {
		return nadawca;
	}

	public void setNadawca(String nadawca) {
		this.nadawca = nadawca;
	}

	public String getTekst() {
		return tekst;
	}

	public void setTekst(String tekst) {
		this.tekst = tekst;
	}

	public Timestamp getCzas() {
		return czas;
	}

	public void setCzas(Timestamp czas) {
		this.czas = czas;
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
		return "Wiadomosc [Nr_wiadomosci=" + Nr_wiadomosci + ", nadawca=" + nadawca + ", tekst=" + tekst + ", czas="
				+ czas + ", Nr_forum=" + Nr_forum + ", Nr_prowadzacego=" + Nr_prowadzacego + ", Nr_studenta="
				+ Nr_studenta + "]";
	}

}
