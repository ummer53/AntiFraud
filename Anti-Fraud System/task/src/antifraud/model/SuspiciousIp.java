package antifraud.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@Table(name="suspicious_ip")
public class SuspiciousIp {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    Long id;
    @Column(name = "ip")
    String ip;

    public SuspiciousIp(String ip) {
        this.ip = ip;
    }
}
