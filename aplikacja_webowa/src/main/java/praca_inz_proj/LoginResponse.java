package praca_inz_proj;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LoginResponse {
	@JsonProperty
	private Student student;
	@JsonProperty
    private Prowadzacy prowadzacy;
	public Student getStudent() {
		return student;
	}
	public void setStudent(Student student) {
		this.student = student;
	}
	public Prowadzacy getProwadzacy() {
		return prowadzacy;
	}
	public void setProwadzacy(Prowadzacy prowadzacy) {
		this.prowadzacy = prowadzacy;
	}
    
    
}
