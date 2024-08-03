package seleksi.labpro.owca.respository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import seleksi.labpro.owca.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
