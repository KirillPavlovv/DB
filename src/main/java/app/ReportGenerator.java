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
        reportGenerator.generateReport(UUID.fromString("dd115157-a995-4b0a-82e1-e3f56a09185e"));
    }

    private void generateReport(UUID customerId) {

        Report report = new Report();
        setCustomerName(customerId, report);
        List<Payment> payments = getPaymentsByCustomerId(customerId);
        List<Invoice> invoices = getInvoicesByCustomerID(customerId);
        List<ReportLine> reportLines = new ArrayList<>();
        report.setBalance(getTotalBalance(payments, invoices));

        for (Invoice invoice : invoices) {
            BigDecimal balance = BigDecimal.ZERO;
            ReportLine reportLine = new ReportLine();
            reportLine.setInvoice(invoice);
            balance = balance.subtract(invoice.getAmount());
            Map<UUID, BigDecimal> map = new HashMap<>();
            for (Payment payment : payments) {
                if(payment.getAmount().compareTo(BigDecimal.ZERO)>0) {
                    balance = balance.add(payment.getAmount());
                    if (balance.compareTo(BigDecimal.ZERO) > 0) {
                        BigDecimal remainder = payment.getAmount().subtract(balance);
                        map.put(payment.getId(), remainder);
                        payment.setAmount(balance);
                        break;
                    } else {
                        map.put(payment.getId(), payment.getAmount());
                        payment.setAmount(BigDecimal.ZERO);
                    }
                }
            }
            reportLine.setPayments(map);
            reportLines.add(reportLine);
            report.setReportLines(reportLines);
        }
        System.out.println(reportLines);
        System.out.println(report);
    }

    private BigDecimal getTotalBalance(List<Payment> payments, List<Invoice> invoices) {
        BigDecimal totalBalance = BigDecimal.ZERO;
        for (Invoice invoice : invoices) {
            totalBalance = totalBalance.subtract(invoice.getAmount());
        }
        for (Payment payment : payments) {
            totalBalance = totalBalance.add(payment.getAmount());
        }
        return totalBalance;
    }

    private void setCustomerName(UUID customerId, Report report) {
        jdbcTemplate.query("SELECT name FROM customers WHERE id= :id", Map.of("id", customerId.toString()), resultSet -> {
            report.setCustomerName(resultSet.getString("name"));
        });
    }

    private List<Invoice> getInvoicesByCustomerID(UUID customerId) {
        return jdbcTemplate.query("SELECT * FROM invoices WHERE customer_id = :id ORDER BY date", Map.of("id", customerId.toString()), (resultSet, rowNum) ->
                new Invoice(UUID.fromString(resultSet.getString("id")), UUID.fromString(resultSet.getString("customer_id")), LocalDate.parse(resultSet.getString("date"), DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss")), new BigDecimal(resultSet.getString("amount"))));
    }

    private List<Payment> getPaymentsByCustomerId(UUID customerId) {
        return jdbcTemplate.query("SELECT * FROM payments WHERE customer_id = :id ORDER BY date", Map.of("id", customerId.toString()), (resultSet, rowNum) ->
                new Payment(UUID.fromString(resultSet.getString("id")), UUID.fromString(resultSet.getString("customer_id")), LocalDate.parse(resultSet.getString("date"), DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss")), new BigDecimal(resultSet.getString("amount"))));
    }


}
