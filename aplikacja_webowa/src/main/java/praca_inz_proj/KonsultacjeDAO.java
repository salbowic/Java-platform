package praca_inz_proj;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class KonsultacjeDAO {

	/* JdbcTemplate */
	@Autowired
	private JdbcTemplate jdbcTemplate;

	/* Constructor for JdbcTemplate */
	public KonsultacjeDAO(JdbcTemplate jdbcTemplate) {
		super();
		this.jdbcTemplate = jdbcTemplate;
	}

	/* List for data from database */
	public List<Konsultacja> list() {

		String sql = "SELECT * FROM Konsultacje";
		List<Konsultacja> listKonsultacja = jdbcTemplate.query(sql,
				BeanPropertyRowMapper.newInstance(Konsultacja.class));
		return listKonsultacja;
	}

	/* (C)reate Insert new record */
	public void save(Konsultacja konsultacja) {
		SimpleJdbcInsert insertActor = new SimpleJdbcInsert(jdbcTemplate);
		insertActor.withTableName("Konsultacje").usingColumns("DataOd", "DataDo", "Nr_pokoju", "Czy_online",
				"Czy_publiczne", "Pytanie", "Nr_realizacji", "Nr_prowadzacego", "Powiadomienie");

		BeanPropertySqlParameterSource param = new BeanPropertySqlParameterSource(konsultacja);
		insertActor.execute(param);
	}
	
	public Konsultacja saveKonsultacja(Konsultacja konsultacja) {
	    SimpleJdbcInsert insertActor = new SimpleJdbcInsert(jdbcTemplate);
	    insertActor.withTableName("Konsultacje").usingColumns("DataOd", "DataDo", "Nr_pokoju", "Czy_online",
				"Czy_publiczne", "Pytanie", "Nr_realizacji", "Nr_prowadzacego", "Powiadomienie");
	    BeanPropertySqlParameterSource param = new BeanPropertySqlParameterSource(konsultacja);
	    insertActor.execute(param);
	    return konsultacja;
	}

	/* (R)ead - data from database */
	public Konsultacja get(int Nr_konsultacji) {
		Object[] args = { Nr_konsultacji };
		String sql = "SELECT * FROM KONSULTACJE WHERE Nr_konsultacji = " + args[0];
		Konsultacja konsultacja = jdbcTemplate.queryForObject(sql,
				BeanPropertyRowMapper.newInstance(Konsultacja.class));

		return konsultacja;
	}

	/* (U)pdate - data inside database */
	public void update(Konsultacja konsultacja) {
		String sql = "UPDATE KONSULTACJE SET DataOd=:DataOd, DataDo=:DataDo, Nr_pokoju=:Nr_pokoju, Czy_online=:Czy_online, "
				+ "Czy_publiczne=:Czy_publiczne, Pytanie=:Pytanie, Powiadomienie=:Powiadomienie WHERE Nr_konsultacji=:Nr_konsultacji";
		BeanPropertySqlParameterSource param = new BeanPropertySqlParameterSource(konsultacja);
		NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(jdbcTemplate);

		template.update(sql, param);
	}

	/* (D)elete - choosen data */
	public void delete(int nr_konsultacji) {
		String sql = "DELETE FROM KONSULTACJE WHERE Nr_konsultacji = ?";
		jdbcTemplate.update(sql, nr_konsultacji);
	}
	
	public void deletePowiadomienie(int nr_konsultacji) {
	    String sql = "UPDATE KONSULTACJE SET powiadomienie = NULL WHERE Nr_konsultacji = ?";
	    jdbcTemplate.update(sql, nr_konsultacji);
	}
	
	public void editPytanie(int nr_konsultacji, String pytanie) {
	    String sql = "UPDATE KONSULTACJE SET pytanie = ? WHERE Nr_konsultacji = ?";;
	    jdbcTemplate.update(sql, pytanie, nr_konsultacji);
	}
	
	public List<Konsultacja> getKonsultacjeByNrProwadzacego(int nr_prowadzacego) {
	    String query = "SELECT * FROM Konsultacje WHERE Nr_prowadzacego = ?";
	    @SuppressWarnings("deprecation")
		List<Konsultacja> konsultacje = jdbcTemplate.query(query, new Object[] { nr_prowadzacego },
	            new RowMapper<Konsultacja>() {
	                @Override
	                public Konsultacja mapRow(ResultSet rs, int rowNum) throws SQLException {
	                    int nr_konsultacji = rs.getInt("Nr_konsultacji");
	                    Timestamp dataOdTimestamp = rs.getTimestamp("DataOd");
	                    Timestamp dataDoTimestamp = rs.getTimestamp("DataDo");
	                    Date dataOd = new Date(dataOdTimestamp.getTime());
	                    Date dataDo = new Date(dataDoTimestamp.getTime());
	                    int nr_pokoju = rs.getInt("Nr_pokoju");
	                    String czy_online = rs.getString("Czy_online");
	                    String czy_publiczne = rs.getString("Czy_publiczne");
	                    String pytanie = rs.getString("Pytanie");
	                    int nr_realizacji = rs.getInt("nr_realizacji");
	                    int nr_prowadzacego = rs.getInt("nr_prowadzacego");
	                    String powiadomienie = rs.getString("Powiadomienie");
	                    return new Konsultacja(nr_konsultacji, dataOd, dataDo, nr_pokoju, czy_online, czy_publiczne, pytanie, nr_realizacji, nr_prowadzacego, powiadomienie, null, null, null);
	                }
	            });
	    return konsultacje;
	}
	
	public List<Konsultacja> getKonsultacjeByNrStudenta(int studentId) {
	    String query = "SELECT * FROM Konsultacje k " +
	                   "INNER JOIN STUDENT_KONSULTACJA sk ON sk.Nr_konsultacji = k.Nr_konsultacji " +
	                   "WHERE sk.Nr_studenta = ?";
	    @SuppressWarnings("deprecation")
		List<Konsultacja> konsultacje = jdbcTemplate.query(query,
	        new Object[] { studentId },
	        new RowMapper<Konsultacja>() {
	            @Override
	            public Konsultacja mapRow(ResultSet rs, int rowNum) throws SQLException {
	                int nr_konsultacji = rs.getInt("Nr_konsultacji");
                    Timestamp dataOdTimestamp = rs.getTimestamp("DataOd");
                    Timestamp dataDoTimestamp = rs.getTimestamp("DataDo");
                    Date dataOd = new Date(dataOdTimestamp.getTime());
                    Date dataDo = new Date(dataDoTimestamp.getTime());
	                int nr_pokoju = rs.getInt("Nr_pokoju");
	                String czy_online = rs.getString("Czy_online");
	                String czy_publiczne = rs.getString("Czy_publiczne");
	                String pytanie = rs.getString("Pytanie");
	                int nr_realizacji = rs.getInt("nr_realizacji");
	                int nr_prowadzacego = rs.getInt("nr_prowadzacego");
	                String powiadomienie = rs.getString("Powiadomienie");
	                return new Konsultacja(nr_konsultacji, dataOd, dataDo, nr_pokoju, czy_online, czy_publiczne, pytanie, nr_realizacji, nr_prowadzacego, powiadomienie, null, null, null);
	            }
	        });
	    return konsultacje;
	}

}
