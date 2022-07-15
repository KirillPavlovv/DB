package app;

import database.Invoice;
import database.Payment;
import database.Report;
import database.ReportLine;
import lombok.RequiredArgsConstructor;
import netscape.javascript.JSObject;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@RequiredArgsConstructor
public class ReportGenerator {

    NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(new DriverManagerDataSource("jdbc:postgresql://localhost:1379/postgres?useSSL=false", "user", "password"));

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

        BigDecimal balance = getBalance(payments, invoices, reportLines);

        report.setReportLines(reportLines);
        report.setReportDate(LocalDate.now());
        report.setBalance(balance);


        List<String> messages = new ArrayList<>();
        for (ReportLine reportLine : reportLines) {
            String message = "";
            StringBuilder stringBuilder = new StringBuilder();
            LocalDate date = reportLine.getInvoice().getDate();
            BigDecimal amount = reportLine.getInvoice().getAmount();
            stringBuilder.append("Invoice date: ").append(date).append(" ").append("Invoice amount").append(amount).append(" ").append("\r\n");;
            Map<Payment, BigDecimal> payments1 = reportLine.getPayments();
            for (Payment payment : payments1.keySet()) {
                BigDecimal paymentAmount = payments1.get(payment);
                LocalDate paymentDate = payment.getDate();
                stringBuilder.append(paymentDate).append(" ").append(paymentAmount).append("\r\n");
            }
            message = stringBuilder.toString();
            messages.add(message);
        }




        System.out.println("!!!"+ messages);

        System.out.println("Customer name: " + report.getCustomerName() + "\n" +
                "Report date: " + report.getReportDate() + "\r\n" +

                messages + "\r\n" +
                "Balance: " + report.getBalance()

        );

//        System.out.println(report);
//        System.out.println(reportLines);
//        System.out.println(invoices);
//        System.out.println(payments);

    }

    private BigDecimal getBalance(List<Payment> payments, List<Invoice> invoices, List<ReportLine> reportLines) {
        BigDecimal balance = null;
        for (Invoice invoice : invoices) {
            ReportLine reportLine = new ReportLine();
            BigDecimal invoiceAmount = invoice.getAmount();
            reportLine.setInvoice(invoice);
            for (Payment payment : payments) {
                BigDecimal paymentAmount = payment.getAmount();
                Map<Payment, BigDecimal> map = new HashMap<>();
                if (paymentAmount.compareTo(invoiceAmount) > 0) {
                    payment.setAmount(paymentAmount.subtract(invoiceAmount));
                    invoice.setAmount(BigDecimal.ZERO);
                    map.put(payment, invoiceAmount);
                    reportLine.setPayments(map);
                    balance = payment.getAmount();
                    break;
                } else {
                    invoice.setAmount(invoiceAmount.subtract(paymentAmount));
                    payment.setAmount(BigDecimal.ZERO);
                    map.put(payment, paymentAmount);
                    reportLine.setPayments(map);
                    balance = BigDecimal.ZERO.subtract(invoice.getAmount());
                    continue;
                }
            }
            reportLines.add(reportLine);
        }
        return balance;
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
