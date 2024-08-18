package seleksi.labpro.owca.service.impl;

import org.springframework.stereotype.Service;
import seleksi.labpro.owca.entity.Film;
import seleksi.labpro.owca.entity.User;
import seleksi.labpro.owca.respository.FilmRepository;
import seleksi.labpro.owca.respository.UserRepository;
import seleksi.labpro.owca.service.WishlistUserService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class WishlistUserServiceImpl implements WishlistUserService {
    private final FilmRepository filmRepository;
    private final UserRepository userRepository;

    public WishlistUserServiceImpl(FilmRepository filmRepository, UserRepository userRepository) {
        this.filmRepository = filmRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void addWishlistToUser(Long userId, Long filmId) {
        Optional<Film> foundFilm = filmRepository.findById(filmId);
        if (foundFilm.isEmpty()) {
            throw new IllegalStateException("Film not found");
        }
        Optional<User> foundUser = userRepository.findById(userId);
        if (foundUser.isEmpty()) {
            throw new IllegalStateException("User not found");
        }
        User user = foundUser.get();
        Film film = foundFilm.get();

        if (!user.getWishlistFilms().contains(film)) {
            user.getWishlistFilms().add(film);
            film.getWishlistedBy().add(user);
            userRepository.save(user);
        }
    }

    @Override
    public Boolean deleteWishlist(Long userId, Long filmId) {
        Optional<Film> foundFilm = filmRepository.findById(filmId);
        if (foundFilm.isEmpty()) {
            throw new IllegalStateException("Film not found");
        }
        Optional<User> foundUser = userRepository.findById(userId);
        if (foundUser.isEmpty()) {
            throw new IllegalStateException("User not found");
        }
        User user = foundUser.get();
        Film film = foundFilm.get();

        if (user.getWishlistFilms().remove(film)) {
            film.getWishlistedBy().remove(user);
            userRepository.save(user);
            return true;
        }
        return  false;
    }

    @Override
    public List<Film> getAllWishlist(Long userId) {
        Optional<User> user = userRepository.findById(userId);

        if (user.isEmpty()) {
            return null;
        }

        return user.get().getWishlistFilms().stream().collect(Collectors.toList());
    }

    @Override
    public Boolean isWishlistedByUserId(Long filmId, Long userId) {
        Optional<Film> foundFilm = filmRepository.findById(filmId);
        if (foundFilm.isEmpty()) {
            return false;
        }
        Optional<User> foundUser = userRepository.findById(userId);
        if (foundUser.isEmpty()) {
            return false;
        }
        User user = foundUser.get();
        Film film = foundFilm.get();

        if (user.getWishlistFilms().contains(film)) {
            return true;
        }
        return false;
    }
}