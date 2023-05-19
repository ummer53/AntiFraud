package antifraud.dto;

import antifraud.model.Transactions;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

public record TransactionInDto(
        long amount,
        @NotBlank String ip,
        @NotBlank String number,
        String region,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        LocalDateTime date
) {

    public Transactions toEntity() {
        return new Transactions(
                amount,
                ip,
                number,
                region,
                date
        );
    }
}
