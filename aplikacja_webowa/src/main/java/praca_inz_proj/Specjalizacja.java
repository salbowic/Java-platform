package praca_inz_proj;

public class Specjalizacja {
	/* Fields from database */

	private int Nr_specjalizacji;
	private String Nazwa_specjalizacji;
	private String Opis;

	public Specjalizacja() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	/* Constructor */
	/**
	 * @param nr_specjalizacji
	 * @param nazwa_specjalizacji
	 * @param opis
	 */
	public Specjalizacja(int nr_specjalizacji, String nazwa_specjalizacji, String opis) {
		super();
		this.Nr_specjalizacji = nr_specjalizacji;
		this.Nazwa_specjalizacji = nazwa_specjalizacji;
		this.Opis = opis;
	}

	/* Getters and setters */
	
	public int getNr_specjalizacji() {
		return Nr_specjalizacji;
	}

	public void setNr_specjalizacji(int nr_specjalizacji) {
		this.Nr_specjalizacji = nr_specjalizacji;
	}

	public String getNazwa_specjalizacji() {
		return Nazwa_specjalizacji;
	}

	public void setNazwa_specjalizacji(String nazwa_specjalizacji) {
		this.Nazwa_specjalizacji = nazwa_specjalizacji;
	}

	public String getOpis() {
		return Opis;
	}

	public void setOpis(String opis) {
		this.Opis = opis;
	}

	/* toString() */
	
	@Override
	public String toString() {
		return "Specjalizacje [Nr_specjalizacji=" + Nr_specjalizacji + ", Nazwa_specjalizacji=" + Nazwa_specjalizacji
				+ ", Opis=" + Opis + "]";
	}
	
	
}
