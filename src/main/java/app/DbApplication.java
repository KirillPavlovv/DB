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

    public static void connectDB(Customer customer) throws SQLException {
        String url = "jdbc:postgresql://localhost:1379/postgres";
        String username = "user";
        String password = "password";

        Connection conn = DriverManager.getConnection(url, username, password);
        Statement stmt = conn.createStatement();
//        ResultSet resultSet = stmt.executeQuery("select * from payments");
//
//        while (resultSet.next()) {
//            System.out.println(resultSet.getString("date") + " " + resultSet.getString("amount"));
//        }

//        String sql = "INSERT INTO customers VALUES (" + customer.getId() + ", " + customer.getName() + ")";
//            stmt.executeQuery(sql);

    }

    public static void generateDB() throws SQLException {
        Random random = new Random();
        for (int i = 1; i < 100; i++) {
            UUID customerId = UUID.randomUUID();
            Customer customer = new Customer(customerId, "Customer" + i);

            connectDB(customer);

            for (int j = 1; j <= 3; j++) {
                getInvoice(random, customerId);
            }
            for (int j = 1; j < random.nextInt(1, 5); j++) {
                getPayment(random);
            }

        }

    }

    private static void getPayment(Random random) {
        UUID paymentId = UUID.randomUUID();
        LocalDate date = LocalDate.now().minusDays(random.nextInt(365));
        BigDecimal amount = BigDecimal.valueOf(random.nextInt(10000));
        Payment payment = new Payment(paymentId, date, amount);

    }

    private static void getInvoice(Random random, UUID customerId) {
        UUID invoiceId = UUID.randomUUID();
        LocalDate date = LocalDate.now().minusDays(random.nextInt(365));
        BigDecimal amount = BigDecimal.valueOf(random.nextInt(10000));
        Invoice invoice = new Invoice(invoiceId, customerId, date, amount);
    }

}
