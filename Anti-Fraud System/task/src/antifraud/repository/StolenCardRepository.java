package antifraud.repository;

import antifraud.model.StolenCard;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface StolenCardRepository extends CrudRepository<StolenCard, Long> {
    public Optional<StolenCard> findByNumber(String number);

    public List<StolenCard> findAllByOrderByIdAsc();
}
