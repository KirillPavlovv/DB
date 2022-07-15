package app;

import database.Report;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import java.util.UUID;

@RequiredArgsConstructor
public class ReportGenerator {

    public static final String SELECT_NAME_FROM_CUSTOMERS_WHERE_ID_CUSTOMER_ID = "SELECT name FROM customers WHERE id= :customerId";
    NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(new DriverManagerDataSource("jdbc:postgresql://localhost:1379/postgres?useSSL=false", "user", "password"));

    public static void main(String[] args) {


        ReportGenerator reportGenerator = new ReportGenerator();
        reportGenerator.generateReport(UUID.fromString("eb4aa1b8-d07f-4132-9f0a-dddffcc33ec8"));
    }

    private void generateReport(UUID customerId)  {

        Report report = new Report();
        jdbcTemplate.query("SELECT name FROM customers WHERE id= :id", Map.of("id", customerId.toString()), resultSet -> {
            report.setCustomerName(resultSet.getString("name"));

        });


    }


}
