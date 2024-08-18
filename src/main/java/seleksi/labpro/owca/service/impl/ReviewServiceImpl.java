package seleksi.labpro.owca.service.impl;

import org.springframework.stereotype.Service;
import seleksi.labpro.owca.entity.Film;
import seleksi.labpro.owca.entity.Review;
import seleksi.labpro.owca.entity.User;
import seleksi.labpro.owca.respository.FilmRepository;
import seleksi.labpro.owca.respository.ReviewRepository;
import seleksi.labpro.owca.respository.UserRepository;
import seleksi.labpro.owca.service.ReviewService;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ReviewServiceImpl implements ReviewService {
    private final ReviewRepository reviewRepository;
    private final FilmRepository filmRepository;
    private final UserRepository userRepository;

    public ReviewServiceImpl(ReviewRepository reviewRepository, FilmRepository filmRepository, UserRepository userRepository) {
        this.reviewRepository = reviewRepository;
        this.filmRepository = filmRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Review addReview(Long filmId, Long userId, String comment, Integer rating) {
        Optional<Film> foundFilm = filmRepository.findById(filmId);
        if (foundFilm.isEmpty()) {
            return null;
        }
        Optional<User> foundUser = userRepository.findById(userId);
        if (foundUser.isEmpty()) {
            return null;
        }

        Review newReview = Review
                .builder()
                .user(foundUser.get())
                .film(foundFilm.get())
                .comment(comment)
                .rating(rating)
                .build();

        return reviewRepository.save(newReview);
    }

    @Override
    public List<Review> getAllReviewById(Long filmId) {
        List<Review> allReviews = reviewRepository.findAll();

        return allReviews.stream()
                .filter(review -> review.getFilm().getId().equals(filmId))
                .collect(Collectors.toList());
    }
}
