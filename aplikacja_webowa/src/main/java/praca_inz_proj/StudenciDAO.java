package praca_inz_proj;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import oracle.jdbc.OracleConnection;
import oracle.sql.BLOB;

@SuppressWarnings("deprecation")
@Repository
public class StudenciDAO {

	/* JdbcTemplate */
	
	private JdbcTemplate jdbcTemplate;
	private DataSource dataSource;

	/* Constructor for JdbcTemplate */
	@Autowired
	public StudenciDAO(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
		this.dataSource = jdbcTemplate.getDataSource();
	}

	/* List for data from database */
	public List<Student> list() {

		String sql = "SELECT * FROM Studenci";
		List<Student> listStudent = jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(Student.class));
		return listStudent;
	}

	/* (C)reate Insert new record */
	public void save(Student student) {
		SimpleJdbcInsert insertActor = new SimpleJdbcInsert(jdbcTemplate);
		insertActor.withTableName("Studenci").usingColumns("Nr_studenta", "Nr_indeksu", "Imie", "Nazwisko", "Plec",
				"Email", "Nr_telefonu");

		BeanPropertySqlParameterSource param = new BeanPropertySqlParameterSource(student);
		insertActor.execute(param);
	}

	/* (R)ead - data from database */
	public Student get(int Nr_studenta) {
		String sql = "SELECT * FROM STUDENCI WHERE Nr_studenta = ?";
		Student student = jdbcTemplate.queryForObject(sql, new Object[] { Nr_studenta },
				BeanPropertyRowMapper.newInstance(Student.class));
		return student;
	}

	/* (U)pdate - data inside database */
	public void update(Student student) {
	    String sql = "UPDATE STUDENCI SET Nr_indeksu=?, Imie=?, Nazwisko=?, Plec=?, Email=?, Nr_telefonu=?, Zdjecie=? WHERE Nr_studenta=?";
	    
	    try (Connection connection = dataSource.getConnection();
	         PreparedStatement statement = connection.prepareStatement(sql)) {
	        statement.setString(1, student.getNr_indeksu());
	        statement.setString(2, student.getImie());
	        statement.setString(3, student.getNazwisko());
	        statement.setString(4, student.getPlec());
	        statement.setString(5, student.getEmail());
	        statement.setString(6, student.getNr_telefonu());
	        
	        if (student.getZdjecie() == null) {
	            statement.setNull(7, Types.BLOB);
	        } else {
	            OracleConnection oracleConnection = connection.unwrap(OracleConnection.class);
	            BLOB blob = BLOB.createTemporary(oracleConnection, true, BLOB.DURATION_SESSION);
	            blob.setBytes(1, student.getZdjecie().getBytes(1, (int) student.getZdjecie().length()));
	            statement.setBlob(7, blob);
	        }
	        
	        statement.setInt(8, student.getNr_studenta());

	        statement.executeUpdate();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}


	/* (D)elete - choosen data */
	public void delete(int Nr_studenta) {
		String sql = "DELETE FROM STUDENCI WHERE Nr_studenta = ?";
		jdbcTemplate.update(sql, Nr_studenta);
	}

	public Student getStudentByEmail(String email) {
		String query = "SELECT * FROM Studenci WHERE Email=?";
		try {
			Student student = jdbcTemplate.queryForObject(query, new Object[] { email },
					BeanPropertyRowMapper.newInstance(Student.class));
			return student;
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}
	
	public List<Student> getStudentsByNrKonsultacji(int nrKonsultacji) {
	    String query = "SELECT s.* FROM Studenci s " +
	                   "INNER JOIN Student_Konsultacja sk ON sk.Nr_studenta = s.Nr_studenta " +
	                   "WHERE sk.Nr_konsultacji = ?";
	    List<Student> students = jdbcTemplate.query(query,
	        new Object[]{nrKonsultacji},
	        new RowMapper<Student>() {
	            @Override
	            public Student mapRow(ResultSet rs, int rowNum) throws SQLException {
	                Student student = new Student();
	                student.setNr_studenta(rs.getInt("Nr_studenta"));
	                student.setNr_indeksu(rs.getString("Nr_indeksu"));
	                student.setImie(rs.getString("Imie"));
	                student.setNazwisko(rs.getString("Nazwisko"));
	                student.setPlec(rs.getString("Plec"));
	                student.setEmail(rs.getString("Email"));
	                student.setNr_telefonu(rs.getString("Nr_telefonu"));
	                student.setZdjecie(rs.getBlob("Zdjecie"));
	                return student;
	            }
	        });
	    return students;
	}
	
	public void updateStudentEmailAndNrTel(int nrStudenta, String email, String nrTel) {
	    String sql = "UPDATE STUDENCI SET Email = ?, Nr_telefonu = ? WHERE Nr_studenta = ?";
	    try {
	        jdbcTemplate.update(sql, email, nrTel, nrStudenta);
	    } catch (DataAccessException e) {
	        e.printStackTrace();
	        throw e;
	    }
	}


}
