package seleksi.labpro.owca.service;

import org.springframework.web.multipart.MultipartFile;
import seleksi.labpro.owca.entity.Film;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface FilmService {
    List<Film> getAllFilms();
    Optional<Film> getFilmById(Long id);
    Film createFilm(Film film, MultipartFile video, MultipartFile coverImage) throws IOException;
    Film updateFilm(Long id, Film updatedFilm, MultipartFile video, MultipartFile coverImage) throws IOException;
    Film deleteFilmById(Long id);
}
