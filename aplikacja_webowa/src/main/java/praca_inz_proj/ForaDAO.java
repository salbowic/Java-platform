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
public class ForaDAO {

	/* JdbcTemplate */
	@Autowired
	private JdbcTemplate jdbcTemplate;

	/* Constructor for JdbcTemplate */
	public ForaDAO(JdbcTemplate jdbcTemplate) {
		super();
		this.jdbcTemplate = jdbcTemplate;
	}

	/* List for data from database */
	public List<Forum> list() {

		String sql = "SELECT * FROM FORA";
		List<Forum> listForum = jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(Forum.class));
		return listForum;
	}

	/* (C)reate Insert new record */
	public void save(Forum forum) {
		SimpleJdbcInsert insertActor = new SimpleJdbcInsert(jdbcTemplate);
		insertActor.withTableName("Fora").usingColumns("Rodzaj");

		BeanPropertySqlParameterSource param = new BeanPropertySqlParameterSource(forum);
		insertActor.execute(param);
	}

	/* (R)ead - data from database */
	public Forum get(int Nr_forum) {
		Object[] args = { Nr_forum };
		String sql = "SELECT * FROM FORA WHERE Nr_forum = " + args[0];
		Forum forum = jdbcTemplate.queryForObject(sql, BeanPropertyRowMapper.newInstance(Forum.class));

		return forum;
	}

	/* (U)pdate - data inside database */
	public void update(Forum forum) {
		String sql = "UPDATE FORA SET Rodzaj=:Rodzaj WHERE Nr_forum=:Nr_forum";
		BeanPropertySqlParameterSource param = new BeanPropertySqlParameterSource(forum);
		NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(jdbcTemplate);

		template.update(sql, param);
	}

	/* (D)elete - choosen data */
	public void delete(int Nr_forum) {
		String sql = "DELETE FROM FORA WHERE Nr_forum = ?";
		jdbcTemplate.update(sql, Nr_forum);
	}

	public String getSemestrByNrRealizacji(int nr_realizacji) {
		String sql = "SELECT semestr FROM realizacje WHERE Nr_realizacji = ?";
		@SuppressWarnings("deprecation")
		String semestr = jdbcTemplate.queryForObject(sql, new Object[] { nr_realizacji }, String.class);
		return semestr != null ? semestr : "";
	}

	@SuppressWarnings("deprecation")
	public String getNazwaSpecjalizacjiByNrSpecjalizacji(int nr_specjalizacji) {
		String sql = "SELECT Nazwa_specjalizacji FROM Specjalizacje WHERE Nr_specjalizacji = ?";
		return jdbcTemplate.queryForObject(sql, new Object[] { nr_specjalizacji }, String.class);
	}

}
