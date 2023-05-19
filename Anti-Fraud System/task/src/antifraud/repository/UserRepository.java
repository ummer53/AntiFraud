package antifraud.repository;

import antifraud.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    public Optional<User> findByUsername(String username);

    public Optional<User> findFirstByRole(String role);
    public List<User> findByRole(String role);
}