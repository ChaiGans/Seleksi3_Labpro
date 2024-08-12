package seleksi.labpro.owca.service;

import seleksi.labpro.owca.entity.Film;
import seleksi.labpro.owca.entity.User;

public interface FilmUserService {
    public void addFilmToUser(User user, Film film);

    public Boolean buyFilmAction(Long userId, Long filmId);
}
