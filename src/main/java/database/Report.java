package database;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Report {

    String customerName;
    List<ReportLine> reportLines;
    LocalDate reportDate;
    BigDecimal balance;
}
