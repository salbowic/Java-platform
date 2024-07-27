package praca_inz_proj;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class PrzedmiotyDAO {

	/* JdbcTemplate */
	@Autowired
	private JdbcTemplate jdbcTemplate;

	/* Constructor for jdbc Template */
	public PrzedmiotyDAO(JdbcTemplate jdbcTemplate) {
		super();
		this.jdbcTemplate = jdbcTemplate;
	}

	/* List for data from database */
	public List<Przedmiot> list() {

		String sql = "SELECT * FROM Przedmioty";
		List<Przedmiot> listPrzedmiot = jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(Przedmiot.class));
		return listPrzedmiot;
	}

	/* (C)reate Insert new record */
	public void save(Przedmiot przedmiot) {
		SimpleJdbcInsert insertActor = new SimpleJdbcInsert(jdbcTemplate);
		insertActor.withTableName("Przedmioty").usingColumns("Nazwa_przedmiotu", "Skrot_przedmiotu",
				"ECTS");
		BeanPropertySqlParameterSource param = new BeanPropertySqlParameterSource(przedmiot);
		insertActor.execute(param);
	}
	
	public Przedmiot savePrzedmiot(Przedmiot przedmiot) {
	    SimpleJdbcInsert insertActor = new SimpleJdbcInsert(jdbcTemplate);
	    insertActor.withTableName("Przedmioty").usingColumns("Nazwa_przedmiotu", "Skrot_przedmiotu", "ECTS");
	    BeanPropertySqlParameterSource param = new BeanPropertySqlParameterSource(przedmiot);
	    insertActor.execute(param);
	    return przedmiot;
	}

	/* (R)ead - data from database */

	public Przedmiot get(int Nr_przedmiotu) {
		Object[] args = { Nr_przedmiotu };
		String sql = "SELECT * FROM PRZEDMIOTY WHERE Nr_przedmiotu = " + args[0];
		Przedmiot przedmiot = jdbcTemplate.queryForObject(sql, BeanPropertyRowMapper.newInstance(Przedmiot.class));

		return przedmiot;
	}

	/* (U)pdate - data inside database */
	public void update(Przedmiot przedmiot) {
		String sql = "UPDATE PRZEDMIOTY SET Nazwa_przedmiotu=:Nazwa_przedmiotu, Skrot_przedmiotu=:Skrot_przedmiotu, "
				+ "ECTS=:ECTS WHERE Nr_przedmiotu=:Nr_przedmiotu";
		BeanPropertySqlParameterSource param = new BeanPropertySqlParameterSource(przedmiot);
		NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(jdbcTemplate);

		template.update(sql, param);
	}

	/* (D)elete - choosen data */
	public void delete(int Nr_przedmiotu) {
		String sql = "DELETE FROM PRZEDMIOTY WHERE Nr_przedmiotu= ?";
		jdbcTemplate.update(sql, Nr_przedmiotu);
	}
	
	public String getNazwaPrzedmiotuByNrForum(String nr_forum) {
	    String sql = "SELECT p.Nazwa_przedmiotu " +
	                 "FROM Przedmioty p " +
	                 "INNER JOIN Realizacje r ON p.Nr_przedmiotu = r.Nr_przedmiotu " +
	                 "INNER JOIN Fora f ON r.Nr_realizacji = f.Nr_realizacji " +
	                 "WHERE f.Nr_forum = ?";
	    @SuppressWarnings("deprecation")
		String nazwa_przedmiotu = jdbcTemplate.queryForObject(sql, new Object[]{nr_forum}, String.class);
	    return nazwa_przedmiotu != null ? nazwa_przedmiotu : "";
	}
	
	public String getPrzedmiotByNrRealizacji(String nr_realizacji) {
	    String sql = "SELECT p.Nazwa_przedmiotu " +
	                 "FROM Przedmioty p " +
	                 "INNER JOIN Realizacje r ON p.Nr_przedmiotu = r.Nr_przedmiotu " +
	                 "WHERE r.Nr_realizacji = ?";
	    @SuppressWarnings("deprecation")
		String nazwa_przedmiotu = jdbcTemplate.queryForObject(sql, new Object[]{nr_realizacji}, String.class);
	    return nazwa_przedmiotu != null ? nazwa_przedmiotu : "";
	}
	
	public String getPrzedmiotAndSemestrByNrRealizacji(String nr_realizacji) {
	    String sql = "SELECT p.Nazwa_przedmiotu, r.semestr " +
	                 "FROM Przedmioty p " +
	                 "INNER JOIN Realizacje r ON p.Nr_przedmiotu = r.Nr_przedmiotu " +
	                 "WHERE r.Nr_realizacji = ?";
	    Map<String, Object> result = jdbcTemplate.queryForMap(sql, new Object[]{nr_realizacji});
	    String nazwa_przedmiotu = (String) result.get("Nazwa_przedmiotu");
	    String semestr = (String) result.get("semestr");
	    return nazwa_przedmiotu != null && semestr != null ? nazwa_przedmiotu + " " + semestr : "";
	}
	
    @SuppressWarnings("deprecation")
	public List<Przedmiot> findByNazwaPrzedmiotuContainingIgnoreCase(String nazwaPrzedmiotu) {
    	String sql = "SELECT * FROM Przedmioty WHERE UPPER(nazwa_przedmiotu) LIKE UPPER(?)";
    	String searchKeyword = "%" + nazwaPrzedmiotu + "%";
    	return jdbcTemplate.query(sql, new Object[]{searchKeyword}, BeanPropertyRowMapper.newInstance(Przedmiot.class));
    }

}
