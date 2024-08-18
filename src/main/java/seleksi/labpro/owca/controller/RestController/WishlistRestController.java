package seleksi.labpro.owca.controller.RestController;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import seleksi.labpro.owca.entity.Film;
import seleksi.labpro.owca.entity.User;
import seleksi.labpro.owca.model.request.WishlistRequest.WishListRequest;
import seleksi.labpro.owca.model.response.RestResponse;
import seleksi.labpro.owca.model.response.Status;
import seleksi.labpro.owca.service.FilmService;
import seleksi.labpro.owca.service.UserService;
import seleksi.labpro.owca.service.WishlistUserService;

import java.util.Optional;

@RestController
@RequestMapping("/wishlists")
@RequiredArgsConstructor
public class WishlistRestController {
    private final WishlistUserService wishlistUserService;
    private final UserService userService;
    private final FilmService filmService;

    @PostMapping("/{id}")
    public ResponseEntity<RestResponse<?>> addNewWishList(@RequestBody WishListRequest request, @PathVariable("id") Long filmId) {
        ResponseEntity<RestResponse<?>> preCheck = checkEntitiesExist(request, filmId);
        if (preCheck != null) return preCheck;

        wishlistUserService.addWishlistToUser(request.getUserId(), filmId);
        return ResponseEntity.ok(
                RestResponse.builder()
                        .status(Status.success)
                        .message("Wishlist is successfully created")
                        .data(null)
                        .build()
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<RestResponse<?>> deleteWishlistById(@RequestBody WishListRequest request, @PathVariable("id") Long filmId) {
        ResponseEntity<RestResponse<?>> preCheck = checkEntitiesExist(request, filmId);
        if (preCheck != null) return preCheck;

        Boolean status = wishlistUserService.deleteWishlist(request.getUserId(), filmId);
        if (!status) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    RestResponse.builder()
                            .status(Status.error)
                            .message("Error when deleting wishlist")
                            .data(null)
                            .build()
            );
        }

        return ResponseEntity.ok(
                RestResponse.builder()
                        .status(Status.success)
                        .message("Wishlist deleted successfully")
                        .data(null)
                        .build()
        );
    }

    private ResponseEntity<RestResponse<?>> checkEntitiesExist(WishListRequest request, Long filmId) {
        Optional<User> foundUser = userService.findUserById(request.getUserId());
        if (foundUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    RestResponse.builder()
                            .status(Status.error)
                            .message("User not found")
                            .data(null)
                            .build()
            );
        }

        Optional<Film> foundFilm = filmService.getFilmById(filmId);
        if (foundFilm.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    RestResponse.builder()
                            .status(Status.error)
                            .message("Film not found")
                            .data(null)
                            .build()
            );
        }
        return null;
    }
}
