package seleksi.labpro.owca.controller.RestController;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import seleksi.labpro.owca.entity.User;
import seleksi.labpro.owca.model.response.RestResponse;
import seleksi.labpro.owca.model.response.Status;
import seleksi.labpro.owca.service.FilmUserService;
import seleksi.labpro.owca.service.UserService;
import seleksi.labpro.owca.utils.JwtService;

@RestController
@RequestMapping("/action")
@RequiredArgsConstructor
public class ActionRestController {
    private final JwtService jwtService;
    private final UserService userService;
    private final FilmUserService filmUserService;

    @PostMapping("/buy/{filmId}")
    public ResponseEntity<RestResponse<?>> buyFilm(@PathVariable("filmId") Long filmId, HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    RestResponse
                            .builder()
                            .status(Status.error)
                            .message("Bearer not found")
                            .data(null)
                            .build()
            );
        }

        String token = authorizationHeader.substring(7);

        String userAuthEmail = jwtService.extractUsername(token);

        User loginUser = userService.findByEmail(userAuthEmail);

        Boolean isPaymentSucceed = filmUserService.buyFilmAction(loginUser.getId(), filmId);

        if (!isPaymentSucceed) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    RestResponse
                            .builder()
                            .status(Status.error)
                            .message("Payment failed")
                            .data(null)
                            .build()
            );
        }

        return ResponseEntity.ok(
                RestResponse
                        .builder()
                        .status(Status.success)
                        .message("Buy action succeeded")
                        .data(isPaymentSucceed)
                        .build()
        );
    }
}
