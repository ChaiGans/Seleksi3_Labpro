package seleksi.labpro.owca.controller.RestController;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import seleksi.labpro.owca.entity.Review;
import seleksi.labpro.owca.model.request.ReviewRequest.PostReviewRequest;
import seleksi.labpro.owca.model.response.RestResponse;
import seleksi.labpro.owca.model.response.ReviewResponse.PostReviewResponse;
import seleksi.labpro.owca.model.response.Status;
import seleksi.labpro.owca.service.ReviewService;

@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor
public class ReviewRestController {
    private final ReviewService reviewService;

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
}
