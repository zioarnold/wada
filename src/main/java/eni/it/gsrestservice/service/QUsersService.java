package eni.it.gsrestservice.service;

import eni.it.gsrestservice.dao.QUsersRepository;
import eni.it.gsrestservice.model.QUsers;
import oracle.jdbc.driver.OracleDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QUsersService {
    @Autowired
    QUsersRepository qUsersRepository;

    public List<QUsers> getAllUsers() {
        return this.qUsersRepository.findAll();
    }

    public QUsers getUserId(String userId) {
        return this.qUsersRepository.getOne(userId);
    }

    public List<QUsers> findQUsers(int currentPage, int recordsPerPage) {
        List<QUsers> users;
        int start = currentPage * recordsPerPage - recordsPerPage;
        String sql = "select * from WADA.Q_USERS WHERE ROWNUM BETWEEN ? and ?";
        SimpleDriverDataSource ds = new SimpleDriverDataSource();
        ds.setDriver(new OracleDriver());
        ds.setUrl("jdbc:oracle:thin:@wada-rdb1-sd.services.eni.intranet:1531:WADAS");
        ds.setUsername("wada");
        ds.setPassword("wada_dev1");
        JdbcTemplate jdbcTemplate = new JdbcTemplate(ds);
        users = jdbcTemplate.query(sql, new Object[]{start, recordsPerPage}, new BeanPropertyRowMapper<>(QUsers.class));
        return users;
    }

    public int getNumberOfRows() {
        int numOfRows = 0;
        String sql = "select count(id) FROM WADA.Q_USERS";
        SimpleDriverDataSource ds = new SimpleDriverDataSource();
        ds.setDriver(new OracleDriver());
        ds.setUrl("jdbc:oracle:thin:@wada-rdb1-sd.services.eni.intranet:1531:WADAS");
        ds.setUsername("wada");
        ds.setPassword("wada_dev1");
        JdbcTemplate jdbcTemplate = new JdbcTemplate(ds);
        if (jdbcTemplate.queryForObject(sql, Integer.class) == null) {
            System.out.println("NULL");
        } else {
            numOfRows = jdbcTemplate.queryForObject(sql, Integer.class);
        }

        return numOfRows;
    }
}
