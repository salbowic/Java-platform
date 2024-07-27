package praca_inz_proj;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class WiadomosciDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void addMessage(Wiadomosc wiadomosc, int nrForum, Integer nrStudenta, Integer nrProwadzacego) {
        String sql = "INSERT INTO wiadomosci (nadawca, tekst, czas, nr_forum, nr_studenta, nr_prowadzacego) VALUES (?, ?, CURRENT_TIMESTAMP, ?, ?, ?)";
        jdbcTemplate.update(sql, wiadomosc.getNadawca(), wiadomosc.getTekst(), nrForum, nrStudenta, nrProwadzacego);
    }

    public List<Wiadomosc> getMessages(int nrForum) {
        String sql = "SELECT * FROM wiadomosci WHERE nr_forum = ? ORDER BY czas ASC";
        List<Wiadomosc> messages = jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(Wiadomosc.class), nrForum);
        return messages;
    }
}