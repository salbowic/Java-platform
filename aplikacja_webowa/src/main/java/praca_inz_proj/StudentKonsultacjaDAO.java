package praca_inz_proj;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class StudentKonsultacjaDAO {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public void save(StudentKonsultacja studentKonsultacja) {
		String sql = "INSERT INTO Student_Konsultacja (Nr_studenta, Nr_konsultacji) VALUES (?, ?)";
		jdbcTemplate.update(sql, studentKonsultacja.getNr_studenta(), studentKonsultacja.getNr_konsultacji());
	}
	
	public void signUp(int nrStudenta, int nrKonsultacji) {
	    if (!checkStudentKonsultacja(nrStudenta, nrKonsultacji)) {
	        String sql = "INSERT INTO Student_Konsultacja (Nr_studenta, Nr_konsultacji) VALUES (?, ?)";
	        jdbcTemplate.update(sql, nrStudenta, nrKonsultacji);
	    } else {
	        System.out.println("Ten Student jest ju¿ zapisany na t¹ konsultacjê");
	    }
	}
	
	public boolean checkStudentKonsultacja(int nrStudenta, int nrKonsultacji) {
	    String sql = "SELECT COUNT(*) FROM Student_Konsultacja WHERE Nr_studenta = ? AND Nr_konsultacji = ?";
	    int count = jdbcTemplate.queryForObject(sql, Integer.class, nrStudenta, nrKonsultacji);
	    return count > 0;
	}

	/* (U)pdate - data inside database */
	public void update(StudentKonsultacja studentKonsultacja) {
		String sql = "UPDATE Student_Konsultacja SET Edycja=:Edycja WHERE Nr_studenta=:Nr_studenta AND Nr_konsultacji=:Nr_konsultacji";
		BeanPropertySqlParameterSource param = new BeanPropertySqlParameterSource(studentKonsultacja);
		NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(jdbcTemplate);

		template.update(sql, param);
	}

	public StudentKonsultacja findByNrStudentaAndNrKonsultacji(int nr_studenta, int nr_konsultacji) {
		String sql = "SELECT * FROM Student_Konsultacja WHERE Nr_studenta = ? AND Nr_konsultacji = ?";
		RowMapper<StudentKonsultacja> rowMapper = new RowMapper<StudentKonsultacja>() {
			@Override
			public StudentKonsultacja mapRow(ResultSet rs, int rowNum) throws SQLException {
				StudentKonsultacja studentKonsultacja = new StudentKonsultacja();
				studentKonsultacja.setNr_studenta(rs.getInt("Nr_studenta"));
				studentKonsultacja.setNr_konsultacji(rs.getInt("Nr_konsultacji"));
				return studentKonsultacja;
			}
		};
		List<StudentKonsultacja> studentKonsultacje = jdbcTemplate.query(sql, rowMapper, nr_studenta, nr_konsultacji);
		if (studentKonsultacje.size() == 0) {
			return null;
		}
		return studentKonsultacje.get(0);
	}

	public List<StudentKonsultacja> getStudentKonsultacjaByNrKonsultacji(int nr_konsultacji) {
		String sql = "SELECT * FROM Student_Konsultacja WHERE Nr_konsultacji = ?";
		RowMapper<StudentKonsultacja> rowMapper = new RowMapper<StudentKonsultacja>() {
			@Override
			public StudentKonsultacja mapRow(ResultSet rs, int rowNum) throws SQLException {
				StudentKonsultacja studentKonsultacja = new StudentKonsultacja();
				studentKonsultacja.setNr_studenta(rs.getInt("Nr_studenta"));
				studentKonsultacja.setNr_konsultacji(rs.getInt("Nr_konsultacji"));
				studentKonsultacja.setEdycja(rs.getString("Edycja"));
				return studentKonsultacja;
			}
		};

		return jdbcTemplate.query(sql, rowMapper, nr_konsultacji);
	}

	public List<StudentKonsultacja> getStudentKonsultacjaByNrStudenta(int nr_studenta) {
		String sql = "SELECT * FROM Student_Konsultacja WHERE Nr_studenta = ?";
		RowMapper<StudentKonsultacja> rowMapper = new RowMapper<StudentKonsultacja>() {
			@Override
			public StudentKonsultacja mapRow(ResultSet rs, int rowNum) throws SQLException {
				StudentKonsultacja studentKonsultacja = new StudentKonsultacja();
				studentKonsultacja.setNr_studenta(rs.getInt("Nr_studenta"));
				studentKonsultacja.setNr_konsultacji(rs.getInt("Nr_konsultacji"));
				studentKonsultacja.setEdycja(rs.getString("Edycja"));
				return studentKonsultacja;
			}
		};

		return jdbcTemplate.query(sql, rowMapper, nr_studenta);
	}

	public void deleteByNrStudentaAndNrKonsultacji(int nrStudenta, int nrKonsultacji) {
		jdbcTemplate.update("DELETE FROM STUDENT_KONSULTACJA WHERE Nr_studenta = ? AND Nr_konsultacji = ?", nrStudenta,
				nrKonsultacji);
	}

	public void deleteEdycjaByNrStudentaAndNrKonsultacji(int nrStudenta, int nrKonsultacji) {
		jdbcTemplate.update("UPDATE STUDENT_KONSULTACJA SET Edycja = NULL WHERE Nr_studenta = ? AND Nr_konsultacji = ?",
				nrStudenta, nrKonsultacji);
	}
}