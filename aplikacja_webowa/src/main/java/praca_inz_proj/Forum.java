package praca_inz_proj;

import org.springframework.lang.Nullable;

public class Forum {
	/* Fields from database */

	private int Nr_forum;
	private String Rodzaj;
	@Nullable
	private Integer Nr_realizacji;
	@Nullable
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
                return "Dla prowadz¹cych";
            case "W":
                return "Dla wszystkich";
            default:
                return "";
        }
    }

}
