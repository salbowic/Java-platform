package praca_inz_proj;

public class StudentForum {
    private int nr_studenta;
    private int nr_forum;
    
	public int getNr_studenta() {
		return nr_studenta;
	}
	public void setNr_studenta(int nr_studenta) {
		this.nr_studenta = nr_studenta;
	}
	public int getNr_forum() {
		return nr_forum;
	}
	public void setNr_forum(int nr_forum) {
		this.nr_forum = nr_forum;
	}
	@Override
	public String toString() {
		return "StudentForum [nr_studenta=" + nr_studenta + ", nr_forum=" + nr_forum + "]";
	}


}
