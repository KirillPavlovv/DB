package database;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Report {

    String customerName;
    LocalDate invoiceDate;
    BigDecimal invoiceAmount;
    Map<LocalDate, BigDecimal> payments;
    LocalDate reportDate;
    BigDecimal balance;
}
