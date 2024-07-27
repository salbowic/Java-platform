package praca_inz_proj;

import java.util.Base64;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;

import oracle.jdbc.OracleConnection;
import oracle.sql.BLOB;

@SuppressWarnings("deprecation")
@Repository
public class ProwadzacyDAO {

	/* JdbcTemplate */

	private JdbcTemplate jdbcTemplate;
	private DataSource dataSource;

	/* Constructor for jdbc Template */
	@Autowired
	public ProwadzacyDAO(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
		this.dataSource = jdbcTemplate.getDataSource();
	}

	/* List for data from database */
	public List<Prowadzacy> list() {

		String sql = "SELECT * FROM Prowadzacy";
		List<Prowadzacy> listProwadzacy = jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(Prowadzacy.class));
		return listProwadzacy;
	}

	/* (C)reate Insert new record */
	public void save(Prowadzacy prowadzacy) {
		SimpleJdbcInsert insertActor = new SimpleJdbcInsert(jdbcTemplate);
		insertActor.withTableName("Prowadzacy").usingColumns("Imie", "Nazwisko", "Plec", "Tytul", "Email",
				"Nr_telefonu","Haslo");
		BeanPropertySqlParameterSource param = new BeanPropertySqlParameterSource(prowadzacy);
		insertActor.execute(param);
	}

	/* (R)ead - data from database */

	public Prowadzacy get(int Nr_prowadzacego) {
	    String sql = "SELECT * FROM PROWADZACY WHERE Nr_prowadzacego = ?";
	    Prowadzacy prowadzacy = jdbcTemplate.queryForObject(sql, new Object[]{Nr_prowadzacego}, BeanPropertyRowMapper.newInstance(Prowadzacy.class));

	    Blob zdjecieBlob = prowadzacy.getZdjecie();
	    String zdjecie64 = convertBlobToBase64(zdjecieBlob);
	    prowadzacy.setZdjecie64(zdjecie64);

	    return prowadzacy;
	}
	
	private String convertBlobToBase64(Blob blob) {
		if (blob == null) {
			return null;
		}

		try (InputStream inputStream = blob.getBinaryStream()) {
			byte[] bytes = inputStream.readAllBytes();
			String base64Image = Base64.getEncoder().encodeToString(bytes);
			return base64Image;
		} catch (SQLException | IOException e) {
			e.printStackTrace();
			return "";
		}
	}

	/* (U)pdate - data inside database */
	public void update(Prowadzacy prowadzacy) {
	    String sql = "UPDATE PROWADZACY SET Imie=?, Nazwisko=?, Plec=?, Tytul=?, Email=?, Nr_telefonu=?, Haslo=?, Zdjecie=? WHERE Nr_prowadzacego=?";
	    
	    try (Connection connection = dataSource.getConnection();
	         PreparedStatement statement = connection.prepareStatement(sql)) {
	        statement.setString(1, prowadzacy.getImie());
	        statement.setString(2, prowadzacy.getNazwisko());
	        statement.setString(3, prowadzacy.getPlec());
	        statement.setString(4, prowadzacy.getTytul());
	        statement.setString(5, prowadzacy.getEmail());
	        statement.setString(6, prowadzacy.getNr_telefonu());
	        statement.setString(7, prowadzacy.getHaslo());
	        
	        if (prowadzacy.getZdjecie() == null) {
	            statement.setNull(8, Types.BLOB);
	        } else {
	            OracleConnection oracleConnection = connection.unwrap(OracleConnection.class);
	            BLOB blob = BLOB.createTemporary(oracleConnection, true, BLOB.DURATION_SESSION);
	            blob.setBytes(1, prowadzacy.getZdjecie().getBytes(1, (int) prowadzacy.getZdjecie().length()));
	            
	            statement.setBlob(8, blob);
	        }
	        
	        statement.setInt(9, prowadzacy.getNr_prowadzacego());
	        statement.executeUpdate();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}
	
	public void updateProwadzacyEmailAndNrTel(int nrProwadzacego, String email, String nrTel) {
	    String sql = "UPDATE PROWADZACY SET Email = ?, Nr_telefonu = ? WHERE Nr_prowadzacego = ?";
	    try {
	        jdbcTemplate.update(sql, email, nrTel, nrProwadzacego);
	    } catch (DataAccessException e) {
	        e.printStackTrace();
	        throw e;
	    }
	}

	/* (D)elete - choosen data */
	public void delete(int Nr_prowadzacego) {
		String sql = "DELETE FROM PROWADZACY WHERE Nr_prowadzacego= ?";
		jdbcTemplate.update(sql, Nr_prowadzacego);
	}
	
	public Prowadzacy getProwadzacyByEmail(String email) {
		String query = "SELECT * FROM PROWADZACY WHERE Email=?";
	    try {
		Prowadzacy prowadzacy = jdbcTemplate.queryForObject(query, new Object[]{email}, BeanPropertyRowMapper.newInstance(Prowadzacy.class));
	    return prowadzacy;
	    } catch (EmptyResultDataAccessException e) {
	        return null;
	    }
	}
	
	public List<Prowadzacy> findByImieAndNazwisko(String fullName) {
	    String[] nameParts = fullName.split(" ");
	    if (nameParts.length == 2) {
	        String imie = nameParts[0];
	        String nazwisko = nameParts[1];
	        return jdbcTemplate.query("SELECT * FROM Prowadzacy WHERE UPPER(Imie) LIKE UPPER(?) AND UPPER(Nazwisko) LIKE UPPER(?)",
	                new Object[]{"%" + imie + "%", "%" + nazwisko + "%"},
	                BeanPropertyRowMapper.newInstance(Prowadzacy.class));
	    } else {
	        String searchKeyword = "%" + fullName.replace(" ", "%") + "%";
	        return jdbcTemplate.query("SELECT * FROM Prowadzacy WHERE UPPER(Imie) LIKE UPPER(?) OR UPPER(Nazwisko) LIKE UPPER(?)",
	                new Object[]{searchKeyword, searchKeyword},
	                BeanPropertyRowMapper.newInstance(Prowadzacy.class));
	    }
	}


}
