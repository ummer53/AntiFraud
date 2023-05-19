package antifraud.repository;

import antifraud.model.SuspiciousIp;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SuspiciousIpRepository extends CrudRepository<SuspiciousIp, Long> {
    public Optional<SuspiciousIp> findByIp(String ip);
    public List<SuspiciousIp> findAllByOrderByIdAsc();
}
