package database;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Report {

    String customerName;
    LocalDate invoiceDate;
    LocalDate paymentDate;
    BigDecimal invoiceAmount;
    BigDecimal paymentAmount;
    LocalDate reportDate;
    BigDecimal balance;
}
