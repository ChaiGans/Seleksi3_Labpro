package seleksi.labpro.owca.service;

import seleksi.labpro.owca.entity.Review;

import java.util.List;

public interface ReviewService {
    Review addReview(Long filmId, Long userId, String comment, Integer rating);
    List<Review> getAllReviewById(Long filmId);
    Boolean deleteReview(Long reviewId);
    Review getReviewById(Long reviewId);
}