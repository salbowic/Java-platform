package praca_inz_proj;

public class StudentKonsultacja {
    private int nr_studenta;
    private int nr_konsultacji;
    private String edycja; 
    
    /**
	 * 
	 */
	public StudentKonsultacja() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param nr_studenta
	 * @param nr_konsultacji
	 * @param edycja
	 */
	public StudentKonsultacja(int nr_studenta, int nr_konsultacji, String edycja) {
		super();
		this.nr_studenta = nr_studenta;
		this.nr_konsultacji = nr_konsultacji;
		this.edycja = edycja;
	}

	public int getNr_studenta() {
        return nr_studenta;
    }

    public void setNr_studenta(int nr_studenta) {
        this.nr_studenta = nr_studenta;
    }

    public int getNr_konsultacji() {
        return nr_konsultacji;
    }

    public void setNr_konsultacji(int nr_konsultacji) {
        this.nr_konsultacji = nr_konsultacji;
    }

	public String getEdycja() {
		return edycja;
	}

	public void setEdycja(String edycja) {
		this.edycja = edycja;
	}

	@Override
	public String toString() {
		return "StudentKonsultacja [nr_studenta=" + nr_studenta + ", nr_konsultacji=" + nr_konsultacji + "]";
	}
    
    
}