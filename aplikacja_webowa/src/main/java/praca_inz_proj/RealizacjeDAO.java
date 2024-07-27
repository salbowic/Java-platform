package praca_inz_proj;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class RealizacjeDAO {

	/* JdbcTemplate */
	@Autowired
	private JdbcTemplate jdbcTemplate;

	/* Constructor for JdbcTemplate */
	public RealizacjeDAO(JdbcTemplate jdbcTemplate) {
		super();
		this.jdbcTemplate = jdbcTemplate;
	}

	/* List for data from database */
	public List<Realizacja> list() {

		String sql = "SELECT * FROM Realizacje";
		List<Realizacja> listRealizacja = jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(Realizacja.class));
		return listRealizacja;
	}

	/* (C)reate Insert new record */
	public void save(Realizacja realizacja) {
		SimpleJdbcInsert insertActor = new SimpleJdbcInsert(jdbcTemplate);
		insertActor.withTableName("Realizacje").usingColumns("Semestr");

		BeanPropertySqlParameterSource param = new BeanPropertySqlParameterSource(realizacja);
		insertActor.execute(param);
	}

	/* (R)ead - data from database */
	public Realizacja get(int Nr_realizacji) {
		Object[] args = { Nr_realizacji };
		String sql = "SELECT * FROM REALIZACJE WHERE Nr_realizacji = " + args[0];
		Realizacja realizacja = jdbcTemplate.queryForObject(sql, BeanPropertyRowMapper.newInstance(Realizacja.class));

		return realizacja;
	}

	/* (U)pdate - data inside database */
	public void update(Realizacja realizacja) {
		String sql = "UPDATE REALIZACJE SET Semestr=:Semestr WHERE Nr_realizacji=:Nr_realizacji";
		BeanPropertySqlParameterSource param = new BeanPropertySqlParameterSource(realizacja);
		NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(jdbcTemplate);

		template.update(sql, param);
	}

	/* (D)elete - choosen data */
	public void delete(int Nr_realizacji) {
		String sql = "DELETE FROM REALIZACJE WHERE Nr_realizacji = ?";
		jdbcTemplate.update(sql, Nr_realizacji);
	}
	
	

}
