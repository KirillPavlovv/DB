package Database_exc.DB;

import database.Customer;
import database.Invoice;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.batch.BatchProperties;

import java.math.BigDecimal;
import java.sql.JDBCType;
import java.time.LocalDate;
import java.util.Random;
import java.util.UUID;

@SpringBootApplication
public class DbApplication {

    public static void main(String[] args) {
        SpringApplication.run(DbApplication.class, args);
    }


    public static void generateDB() {

        for (int i = 1; i < 10; i++) {
            UUID customerId = UUID.randomUUID();
            Customer customer = new Customer(customerId, "Customer" + i);
            for (int j = 1; j < 3; j++) {
                UUID invoiceId = UUID.randomUUID();
                Random random = new Random();
                LocalDate date = LocalDate.now().minusDays(random.nextInt(365));
                BigDecimal amount = BigDecimal.valueOf(random.nextInt(1000));
                Invoice invoice = new Invoice(invoiceId, customerId, date, amount);
            }


        }


    }

}
