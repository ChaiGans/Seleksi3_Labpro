package seleksi.labpro.owca.service;

import seleksi.labpro.owca.entity.Film;

import java.util.List;

public interface WishlistUserService {
    void addWishlistToUser(Long userId, Long filmId);
    Boolean deleteWishlist(Long userId, Long filmId);
    List<Film> getAllWishlist(Long userId);
    Boolean isWishlistedByUserId(Long filmId, Long userId);
}
