package app;

import database.Customer;
import database.Invoice;
import database.Payment;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Random;
import java.util.UUID;


@Component
@RequiredArgsConstructor
public class Batch implements CommandLineRunner {
    public final NamedParameterJdbcTemplate jdbcTemplate;
    private final JdbcTemplate jdbc;

    @Override
    public void run(String... args) {
        generateDB();
    }

    public void generateDB() {
        clearTables();
        Random random = new Random();
        for (int i = 1; i <= 100; i++) {
            UUID customerId = UUID.randomUUID();
            String name = generateName(random);
            jdbcTemplate.update("INSERT INTO customers VALUES ( :id, :name)"
                    , new BeanPropertySqlParameterSource(new Customer(customerId, name)));
            for (int j = 1; j <= 3; j++) {
                getInvoice(random, customerId);
            }
            for (int j = 1; j < random.nextInt(1, 5); j++) {
                getPayment(random, customerId);
            }
        }
    }

    private void clearTables() {
        jdbc.execute("DELETE FROM payments WHERE id IS NOT NULL");
        jdbc.execute("DELETE FROM invoices WHERE id IS NOT NULL");
        jdbc.execute("DELETE FROM customers WHERE id IS NOT NULL");
    }

    private String generateName(Random random) {
        List<String> names = List.of("Juhan", "Andrei", "Jaanus", "Erik", "Mari", "Anna", "Maria", "Julia", "Kristina", "Mait");
        List<String> surnames = List.of("Tamm", "Kask", "Rebane", "Karu", "Hunt", "Meri", "Trump", "Kallas", "Kaljulaid", "Petrov");
        int nameIndex = random.nextInt(names.size());
        int surnameIndex = random.nextInt(surnames.size());
        return names.get(nameIndex) + " " + surnames.get(surnameIndex);
    }

    private void getPayment(Random random, UUID customerId) {
        UUID paymentId = UUID.randomUUID();
        LocalDate date = LocalDate.now().minusDays(random.nextInt(365));
        BigDecimal amount = BigDecimal.valueOf(random.nextInt(10000));
        Payment payment = new Payment(paymentId, customerId, date, amount);
        jdbcTemplate.update("INSERT INTO payments VALUES (:id, :customerId, :date, :amount )", new BeanPropertySqlParameterSource(payment));
    }

    private void getInvoice(Random random, UUID customerId) {
        UUID invoiceId = UUID.randomUUID();
        LocalDate date = LocalDate.now().minusDays(random.nextInt(365));
        BigDecimal amount = BigDecimal.valueOf(random.nextInt(10000));
        Invoice invoice = new Invoice(invoiceId, customerId, date, amount);
        jdbcTemplate.update("INSERT INTO invoices VALUES (:id, :customerId, :date, :amount )", new BeanPropertySqlParameterSource(invoice));
    }
}
