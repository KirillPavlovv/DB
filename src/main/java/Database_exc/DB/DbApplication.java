package Database_exc.DB;

import com.zaxxer.hikari.HikariConfig;
import database.Customer;
import database.Invoice;
import database.Payment;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.Random;
import java.util.UUID;

@SpringBootApplication
public class DbApplication {


    public static void main(String[] args) {
        generateDB();


//        SpringApplication.run(DbApplication.class, args);
    }

    public static void connectDB(Customer customer) throws SQLException {
        String url = "jdbc:postgresql://localhost:1369/postgres";
        String username = "user";
        String password = "password";

        HikariConfig con = new HikariConfig();
        con.setJdbcUrl(url);
        con.setUsername(username);
        con.setPassword(password);

//        DataSource dataSource = new DataSource(con);
//        dataSource.getConnection();
//        dataSource.


        try(Connection conn = DriverManager.getConnection(url, username, password);
            Statement stmt = conn.createStatement();

        ) {
            String sql = "INSERT INTO customer VALUES (" + customer.getId()+", " + customer.getName() +")";
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        }

        public static void generateDB() {

        Connection connection;
        Random random = new Random();
        for (int i = 1; i < 100; i++) {
            UUID customerId = UUID.randomUUID();
            Customer customer = new Customer(customerId, "Customer" + i);

//            connectDB(customer);

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
