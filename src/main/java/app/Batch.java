package app;

import database.Customer;
import database.Invoice;
import database.Payment;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class Batch implements CommandLineRunner {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public void run(String... args) {
        List<Map<String, Object>> result = jdbcTemplate.queryForList("SELECT * FROM customers");
        generateDB();
//        String id = customer.getId().toString();
//        String name = customer.getName().toString();
//        jdbcTemplate.execute("INSERT INTO customers VALUES ( " + customer.getId() + "," + customer.getName() + ")");
//        jdbcTemplate.execute("INSERT INTO customers VALUES ('123','name')");

//        System.out.println(result);
    }

    public void generateDB() {
        Random random = new Random();
        for (int i = 1; i < 100; i++) {
            UUID customerId = UUID.randomUUID();
            Customer customer = new Customer(customerId, "Customer" + i);
            jdbcTemplate.execute("INSERT INTO customers VALUES ( '" + customerId + "','" + customer.getName() + "')");


            for (int j = 1; j <= 3; j++) {
                getInvoice(random, customerId);
            }
            for (int j = 1; j < random.nextInt(1, 5); j++) {
                getPayment(random);
            }

        }
    }

    private  void getPayment(Random random) {
        UUID paymentId = UUID.randomUUID();
        LocalDate date = LocalDate.now().minusDays(random.nextInt(365));
        BigDecimal amount = BigDecimal.valueOf(random.nextInt(10000));
        Payment payment = new Payment(paymentId, date, amount);
        jdbcTemplate.execute("INSERT INTO payments VALUES ( '" + paymentId + "','" + payment.getDate() + "','" + payment.getAmount() + "')");


    }

    private  void getInvoice(Random random, UUID customerId) {
        UUID invoiceId = UUID.randomUUID();
        LocalDate date = LocalDate.now().minusDays(random.nextInt(365));
        BigDecimal amount = BigDecimal.valueOf(random.nextInt(10000));
        Invoice invoice = new Invoice(invoiceId, customerId, date, amount);
        jdbcTemplate.execute("INSERT INTO invoices VALUES ( '" + invoiceId + "','" + invoice.getCustomerId() + "','" + invoice.getDate() + "','" + invoice.getAmount() + "')");
    }
}
