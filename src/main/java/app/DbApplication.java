package app;

import database.Customer;
import database.Invoice;
import database.Payment;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.util.Random;
import java.util.UUID;

@SpringBootApplication
public class DbApplication {


    public static void main(String[] args) throws SQLException {

        SpringApplication.run(DbApplication.class, args);
//        generateDB();
    }


}
