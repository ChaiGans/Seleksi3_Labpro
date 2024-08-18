package seleksi.labpro.owca.controller.RestController;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import seleksi.labpro.owca.entity.Review;
import seleksi.labpro.owca.entity.User;
import seleksi.labpro.owca.model.request.ReviewRequest.PostReviewRequest;
import seleksi.labpro.owca.model.response.RestResponse;
import seleksi.labpro.owca.model.response.ReviewResponse.PostReviewResponse;
import seleksi.labpro.owca.model.response.Status;
import seleksi.labpro.owca.service.ReviewService;
import seleksi.labpro.owca.service.UserService;
import seleksi.labpro.owca.utils.JwtService;

import javax.swing.text.html.Option;
import java.util.Optional;

@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor
public class ReviewRestController {
    private final ReviewService reviewService;
    private final JwtService jwtService;
    private final UserService userService;

    @PostMapping
    public ResponseEntity<RestResponse<?>> createReview(@RequestBody PostReviewRequest request) {
        try {
            Review newReview = reviewService.addReview(request.getFilmId(), request.getUserId(), request.getComment(), request.getRating());
            if (newReview == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                        RestResponse.builder()
                                .status(Status.error)
                                .message("Failed to create new review due to invalid data.")
                                .data(null)
                                .build()
                );
            }

            return ResponseEntity.ok(
                    RestResponse.builder()
                            .status(Status.success)
                            .message("New review created successfully.")
                            .data(PostReviewResponse.builder()
                                    .id(newReview.getId())
                                    .userId(newReview.getUser().getId())
                                    .filmId(newReview.getFilm().getId())
                                    .comment(request.getComment())
                                    .rating(request.getRating())
                                    .build())
                            .build()
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    RestResponse.builder()
                            .status(Status.error)
                            .message("Server error: " + e.getMessage())
                            .data(null)
                            .build()
            );
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<RestResponse<?>> deleteReview(@PathVariable("id") Long id, HttpServletRequest request) {
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

        Review foundReview = reviewService.getReviewById(id);

        if (foundReview == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    RestResponse
                            .builder()
                            .status(Status.error)
                            .message("Review not found")
                            .data(null)
                            .build()
            );
        }

        if (foundReview.getUser().getId() != loginUser.getId()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    RestResponse
                            .builder()
                            .status(Status.error)
                            .message("Could not modify others review.")
                            .data(null)
                            .build()
            );
        }

        if (reviewService.deleteReview(id)) {
            return ResponseEntity.ok(
                    RestResponse.builder()
                            .status(Status.success)
                            .message("Review deleted successfully.")
                            .data(null)
                            .build());
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    RestResponse.builder()
                            .status(Status.error)
                            .message("Failed to delete review.")
                            .data(null)
                            .build()
            );
        }
    }
}
