package app;

import database.Invoice;
import database.Payment;
import database.Report;
import database.ReportLine;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@RequiredArgsConstructor
public class ReportGenerator {

    NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(new DriverManagerDataSource("jdbc:postgresql://localhost:5432/DB", "postgres", "student123"));

    public static void main(String[] args) {


        ReportGenerator reportGenerator = new ReportGenerator();
        reportGenerator.generateReport(UUID.fromString("57821a52-0211-431e-8717-36e370350063"));
    }

    private void generateReport(UUID customerId) {

        Report report = new Report();
        jdbcTemplate.query("SELECT name FROM customers WHERE id= :id", Map.of("id", customerId.toString()), resultSet -> {
            report.setCustomerName(resultSet.getString("name"));
        });
        List<Payment> payments = getPaymentsByCustomerId(customerId);
        List<Invoice> invoices = getInvoicesByCustomerID(customerId);
        List<ReportLine> reportLines = new ArrayList<>();
        BigDecimal balance = BigDecimal.ZERO;

        for (Invoice invoice : invoices) {
            ReportLine reportLine = new ReportLine();
            balance = balance.subtract(invoice.getAmount());
            reportLine.setInvoice(invoice);
            Map<Payment, BigDecimal> map = new HashMap<>();
            for (Payment payment : payments) {
                balance = balance.add(payment.getAmount());
                if (balance.compareTo(BigDecimal.ZERO) > 0) {
                    BigDecimal remainder = payment.getAmount().subtract(balance);
                    map.put(payment, remainder);
                    break;
                } else {
                    map.put(payment, payment.getAmount());
                    payments.remove(payment);
                    continue;
                }

            }
            reportLine.setPayments(map);

            reportLines.add(reportLine);
        }

        System.out.println(reportLines);
//
//
//        System.out.println(invoices);
//        System.out.println(payments);

    }

    private List<Invoice> getInvoicesByCustomerID(UUID customerId) {
        List<Invoice> invoices = jdbcTemplate.query("SELECT * FROM invoices WHERE customer_id = :id ORDER BY date", Map.of("id", customerId.toString()), (resultSet, rowNum) ->
                new Invoice(UUID.fromString(resultSet.getString("id")), UUID.fromString(resultSet.getString("customer_id")), LocalDate.parse(resultSet.getString("date"), DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss")), new BigDecimal(resultSet.getString("amount"))));
        return invoices;
    }

    private List<Payment> getPaymentsByCustomerId(UUID customerId) {
        List<Payment> payments = jdbcTemplate.query("SELECT * FROM payments WHERE customer_id = :id ORDER BY date", Map.of("id", customerId.toString()), (resultSet, rowNum) ->
                new Payment(UUID.fromString(resultSet.getString("id")), UUID.fromString(resultSet.getString("customer_id")), LocalDate.parse(resultSet.getString("date"), DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss")), new BigDecimal(resultSet.getString("amount"))));
        return payments;
    }


}
