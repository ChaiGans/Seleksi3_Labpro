package seleksi.labpro.owca.respository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import seleksi.labpro.owca.entity.Film;

@Repository
public interface FilmRepository extends JpaRepository<Film, Long> {
}
