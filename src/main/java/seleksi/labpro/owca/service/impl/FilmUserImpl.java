package seleksi.labpro.owca.service.impl;

import org.springframework.stereotype.Service;
import seleksi.labpro.owca.entity.Film;
import seleksi.labpro.owca.entity.User;
import seleksi.labpro.owca.respository.FilmRepository;
import seleksi.labpro.owca.respository.UserRepository;
import seleksi.labpro.owca.service.FilmUserService;

import java.util.Optional;

@Service
public class FilmUserImpl implements FilmUserService {
    private final UserRepository userRepository;
    private final FilmRepository filmRepository;

    public FilmUserImpl(UserRepository userRepository, FilmRepository filmRepository) {
        this.userRepository = userRepository;
        this.filmRepository = filmRepository;
    }

    @Override
    public void addFilmToUser(User user, Film film) {
        user.getOwnedFilms().add(film);
        film.getUsers().add(user);

        userRepository.save(user);

    }

    @Override
    public Boolean buyFilmAction(Long userId, Long filmId) {
        Optional<User> foundUser = userRepository.findById(userId);
        Optional<Film> foundFilm = filmRepository.findById(filmId);

        if (foundUser.isPresent() && foundFilm.isPresent()) {
            // User money is enough
            if (foundUser.get().getBalance() >= foundFilm.get().getPrice()) {
                addFilmToUser(foundUser.get(), foundFilm.get());

                // Deduct user money
                foundUser.get().setBalance(foundUser.get().getBalance() - foundFilm.get().getPrice());

                // Save to entity
                userRepository.save(foundUser.get());

                return Boolean.TRUE;
            }
        }

        return Boolean.FALSE;
    }
}
