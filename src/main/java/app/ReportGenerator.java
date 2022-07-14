package app;

import database.Report;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.UUID;

@RequiredArgsConstructor
public class ReportGenerator {

    private final NamedParameterJdbcTemplate jdbcTemplate;
    public static void main(String[] args) {


    }

    public void generateReport(UUID customerId) {

        Report report = new Report();
        jdbcTemplate.query("SELECT name FROM customers WHERE id= :customerId",resultSet -> {

            String name = resultSet.getString("name");
            System.out.println(name);
        });

    }


}
