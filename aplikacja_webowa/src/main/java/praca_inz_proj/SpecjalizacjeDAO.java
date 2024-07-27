package praca_inz_proj;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class SpecjalizacjeDAO {

	/* JdbcTemplate */
	@Autowired
	private JdbcTemplate jdbcTemplate;

	/* Constructor for JdbcTemplate */
	public SpecjalizacjeDAO(JdbcTemplate jdbcTemplate) {
		super();
		this.jdbcTemplate = jdbcTemplate;
	}

	/* List for data from database */
	public List<Specjalizacja> list() {

		String sql = "SELECT * FROM Specjalizacje";
		List<Specjalizacja> listSpecjalizacja = jdbcTemplate.query(sql,
				BeanPropertyRowMapper.newInstance(Specjalizacja.class));
		return listSpecjalizacja;
	}

	/* (C)reate Insert new record */
	public void save(Specjalizacja specjalizacja) {
		SimpleJdbcInsert insertActor = new SimpleJdbcInsert(jdbcTemplate);
		insertActor.withTableName("Specjalizacje").usingColumns("Nazwa_specjalizacji", "Opis");

		BeanPropertySqlParameterSource param = new BeanPropertySqlParameterSource(specjalizacja);
		insertActor.execute(param);
	}

	/* (R)ead - data from database */
	public Specjalizacja get(int Nr_specjalizacji) {
		Object[] args = { Nr_specjalizacji };
		String sql = "SELECT * FROM SPECJALIZACJE WHERE Nr_specjalizacji = " + args[0];
		Specjalizacja specjalizacja = jdbcTemplate.queryForObject(sql,
				BeanPropertyRowMapper.newInstance(Specjalizacja.class));

		return specjalizacja;
	}

	/* (U)pdate - data inside database */
	public void update(Specjalizacja specjalizacja) {
		String sql = "UPDATE SPECJALIZACJE SET Nazwa_specjalizacji=:Nazwa_specjalizacji, Opis=:Opis "
				+ "WHERE Nr_specjalizacji=: Nr_specjalizacji";
		BeanPropertySqlParameterSource param = new BeanPropertySqlParameterSource(specjalizacja);
		NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(jdbcTemplate);

		template.update(sql, param);
	}

	/* (D)elete - choosen data */
	public void delete(int Nr_specjalizacji) {
		String sql = "DELETE FROM SPECJALIZACJE WHERE Nr_specjalizacji = ?";
		jdbcTemplate.update(sql, Nr_specjalizacji);
	}
	
	public Specjalizacja getSpecjalizacjaByNrProwadzacego(int Nr_prowadzacego) {
	    String query = "SELECT s.* FROM specjalizacje s "
	                 + "JOIN prowadzacy_specjalizacja ps ON s.nr_specjalizacji = ps.nr_specjalizacji "
	                 + "WHERE ps.nr_prowadzacego = ?";
	    try {
	        @SuppressWarnings("deprecation")
			Specjalizacja specjalizacja = jdbcTemplate.queryForObject(query, new Object[]{Nr_prowadzacego}, BeanPropertyRowMapper.newInstance(Specjalizacja.class));
	        return specjalizacja;
	    } catch (EmptyResultDataAccessException e) {
	        return null;
	    }
	}


}
