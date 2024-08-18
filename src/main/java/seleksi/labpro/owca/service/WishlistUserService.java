package seleksi.labpro.owca.service;

public interface WishlistUserService {
    public void addWishlistToUser(Long userId, Long filmId);
    public void deleteWishlist(Long userId, Long filmId);
}
