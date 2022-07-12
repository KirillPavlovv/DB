import lombok.Value;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Value
public class Payment {
    UUID id;
    LocalDate date;
    BigDecimal amount;
}
