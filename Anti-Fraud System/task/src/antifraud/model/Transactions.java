package antifraud.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@Table(name = "transactions")
public class Transactions {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    Long id;
    @Column(name = "amount")
    Long amount;
    @Column(name = "ip")
    String ip;
    @Column(name = "number")
    String number;
    @Column(name = "region")
    String region;
    @Column(name = "date")
    LocalDateTime date;

    public Transactions(Long amount, String ip, String number, String region, LocalDateTime date) {
        this.amount = amount;
        this.ip = ip;
        this.number = number;
        this.region = region;
        this.date = date;
    }
}
