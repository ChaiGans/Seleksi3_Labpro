package seleksi.labpro.owca.respository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import seleksi.labpro.owca.entity.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username);

    @Query(value = "SELECT * FROM users WHERE username LIKE CONCAT(:query, '%')", nativeQuery = true)
    List<User> findUsersByUsernameStartingWith(@Param("query") String query);
}
