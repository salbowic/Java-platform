package praca_inz_proj;

public class Realizacja {
	/* Fields from database */

	private int Nr_realizacji;
	private String Semestr;
	private int Nr_przedmiotu;
	
	/**
	 * 
	 */
	public Realizacja() {
		super();
		// TODO Auto-generated constructor stub
	}

	
	/**
	 * @param nr_realizacji
	 * @param semestr
	 * @param nr_przedmiotu
	 */
	public Realizacja(int nr_realizacji, String semestr, int nr_przedmiotu) {
		super();
		Nr_realizacji = nr_realizacji;
		Semestr = semestr;
		Nr_przedmiotu = nr_przedmiotu;
	}

	/* Getters and setters*/
	
	public int getNr_realizacji() {
		return Nr_realizacji;
	}

	public void setNr_realizacji(int nr_realizacji) {
		this.Nr_realizacji = nr_realizacji;
	}

	public String getSemestr() {
		return Semestr;
	}

	public void setSemestr(String semestr) {
		this.Semestr = semestr;
	}
	
	public int getNr_przedmiotu() {
		return Nr_przedmiotu;
	}


	public void setNr_przedmiotu(int nr_przedmiotu) {
		Nr_przedmiotu = nr_przedmiotu;
	}

	/* toString() */
	@Override
	public String toString() {
		return "Realizacja [Nr_realizacji=" + Nr_realizacji + ", Semestr=" + Semestr + ", Nr_przedmiotu="
				+ Nr_przedmiotu + "]";
	}
	
}
