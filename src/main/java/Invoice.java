import lombok.Value;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Value
public class Invoice {
        UUID id;
        UUID customerId;
        LocalDate date;
        BigDecimal amount;
        }
