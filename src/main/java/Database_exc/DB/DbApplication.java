package Database_exc.DB;

import database.Customer;
import database.Invoice;
import database.Payment;
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
        generateDB();


//        SpringApplication.run(DbApplication.class, args);
    }


    public static void generateDB() {
        Random random = new Random();

        for (int i = 1; i < 100; i++) {
            UUID customerId = UUID.randomUUID();
            Customer customer = new Customer(customerId, "Customer" + i);
            System.out.println(customer.toString());
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
        System.out.println(payment.toString());
    }

    private static void getInvoice(Random random, UUID customerId) {
        UUID invoiceId = UUID.randomUUID();
        LocalDate date = LocalDate.now().minusDays(random.nextInt(365));
        BigDecimal amount = BigDecimal.valueOf(random.nextInt(10000));
        Invoice invoice = new Invoice(invoiceId, customerId, date, amount);
        System.out.println(invoice.toString());
    }

}
