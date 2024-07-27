package praca_inz_proj;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.Nullable;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Konsultacja {
	/* Fields from database */
	@JsonProperty("Nr_konsultacji")
	private int nr_konsultacji;
	@JsonProperty("DataOd")
	@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
	private Date DataOd;
	@JsonProperty("DataDo")
	@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
	private Date DataDo;
	@JsonProperty("Nr_pokoju")
	@Nullable
	private Integer Nr_pokoju;
	@JsonProperty("Czy_online")
	private String Czy_online;
	@JsonProperty("Czy_publiczne")
	private String Czy_publiczne;
	@JsonProperty("Pytanie")
	@Nullable
	private String Pytanie;
	@JsonProperty("Nr_realizacji")
	@Nullable
	private Integer Nr_realizacji;
	@JsonProperty("Nr_prowadzacego")
	private int Nr_prowadzacego;
	@JsonProperty("Powiadomienie")
	private String Powiadomienie;

	private List<Student> ZapisaniStudenci;

	private String formattedDataOd;
	private String formattedDataDo;

	/**
	 * 
	 */
	public Konsultacja() {
		super();
		// TODO Auto-generated constructor stub
	}
	/* Constructor */
	public int getNr_konsultacji() {
		return nr_konsultacji;
	}

	/**
	 * @param nr_konsultacji
	 * @param dataOd
	 * @param dataDo
	 * @param nr_pokoju
	 * @param czy_online
	 * @param czy_publiczne
	 * @param pytanie
	 * @param nr_realizacji
	 * @param nr_prowadzacego
	 * @param powiadomienie
	 * @param zapisaniStudenci
	 * @param formattedDataOd
	 * @param formattedDataDo
	 */
	public Konsultacja(int nr_konsultacji, Date dataOd, Date dataDo, Integer nr_pokoju, String czy_online,
			String czy_publiczne, String pytanie, Integer nr_realizacji, int nr_prowadzacego, String powiadomienie,
			List<Student> zapisaniStudenci, String formattedDataOd, String formattedDataDo) {
		super();
		this.nr_konsultacji = nr_konsultacji;
		DataOd = dataOd;
		DataDo = dataDo;
		Nr_pokoju = nr_pokoju;
		Czy_online = czy_online;
		Czy_publiczne = czy_publiczne;
		Pytanie = pytanie;
		Nr_realizacji = nr_realizacji;
		Nr_prowadzacego = nr_prowadzacego;
		Powiadomienie = powiadomienie;
		ZapisaniStudenci = zapisaniStudenci;
		this.formattedDataOd = formattedDataOd;
		this.formattedDataDo = formattedDataDo;
	}

	public void setNr_konsultacji(int nr_konsultacji) {
		this.nr_konsultacji = nr_konsultacji;
	}

	public java.util.Date getDataOd() {
		return DataOd;
	}

	public void setDataOd(java.util.Date dataOd) {
		this.DataOd = dataOd;
	}

	public java.util.Date getDataDo() {
		return DataDo;
	}

	public void setDataDo(java.util.Date dataDo) {
		this.DataDo = dataDo;
	}

	public Integer getNr_pokoju() {
		return Nr_pokoju;
	}

	public void setNr_pokoju(Integer nr_pokoju) {
		this.Nr_pokoju = nr_pokoju;
	}

	public String getCzy_online() {
		return Czy_online;
	}

	public void setCzy_online(String czy_online) {
		this.Czy_online = czy_online;
	}

	public String getCzy_publiczne() {
		return Czy_publiczne;
	}

	public void setCzy_publiczne(String czy_publiczne) {
		this.Czy_publiczne = czy_publiczne;
	}

	public String getPytanie() {
		return Pytanie;
	}

	public void setPytanie(String pytanie) {
		this.Pytanie = pytanie;
	}

	public Integer getNr_realizacji() {
		return Nr_realizacji;
	}

	public void setNr_realizacji(Integer nr_realizacji) {
		Nr_realizacji = nr_realizacji;
	}

	public int getNr_prowadzacego() {
		return Nr_prowadzacego;
	}

	public void setNr_prowadzacego(int nr_prowadzacego) {
		Nr_prowadzacego = nr_prowadzacego;
	}

	public String getPowiadomienie() {
		return Powiadomienie;
	}

	public void setPowiadomienie(String powiadomienie) {
		Powiadomienie = powiadomienie;
	}

	public List<Student> getZapisaniStudenci() {
		return ZapisaniStudenci;
	}

	public void setZapisaniStudenci(List<Student> zapisaniStudenci) {
		ZapisaniStudenci = zapisaniStudenci;
	}

	public String getFormattedDataOd() {
		return formattedDataOd;
	}

	public void setFormattedDataOd(String formattedDataOd) {
		this.formattedDataOd = formattedDataOd;
	}

	public String getFormattedDataDo() {
		return formattedDataDo;
	}

	public void setFormattedDataDo(String formattedDataDo) {
		this.formattedDataDo = formattedDataDo;
	}

	@Override
	public String toString() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dataOdString = dateFormat.format(DataOd);
		String dataDoString = dateFormat.format(DataDo);

		return "Konsultacja [nr_konsultacji=" + nr_konsultacji + ", DataOd=" + dataOdString + ", DataDo=" + dataDoString
				+ ", Nr_pokoju=" + Nr_pokoju + ", Czy_online=" + Czy_online + ", Czy_publiczne=" + Czy_publiczne
				+ ", Pytanie=" + Pytanie + ", Nr_realizacji=" + Nr_realizacji + ", Nr_prowadzacego=" + Nr_prowadzacego
				+ "]";
	}

	public String getFullOnline() {
		switch (this.Czy_online) {
		case "T":
			return "Tak";
		case "N":
			return "Nie";
		default:
			return "";
		}
	}

	public String getFullPubliczne() {
		switch (this.Czy_publiczne) {
		case "T":
			return "Tak";
		case "N":
			return "Nie";
		default:
			return "";
		}
	}

}
