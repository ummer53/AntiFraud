package antifraud.repository;

import antifraud.model.Transactions;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface TransactionRepository extends CrudRepository<Transactions, Long> {
    List<Transactions> findByNumberAndDateBetween(String number, LocalDateTime lastHour, LocalDateTime now);

    List<Transactions> findByNumber(String number);
}
