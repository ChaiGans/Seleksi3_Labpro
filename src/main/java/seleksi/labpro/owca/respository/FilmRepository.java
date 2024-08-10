package seleksi.labpro.owca.respository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import seleksi.labpro.owca.entity.Film;

import java.util.List;

@Repository
public interface FilmRepository extends JpaRepository<Film, Long> {
    @Query(value = "SELECT * FROM films WHERE LOWER(title) LIKE LOWER(CONCAT('%', :query, '%'))", nativeQuery = true)
    List<Film> findUsersByTitleContains(@Param("query") String query);
}
