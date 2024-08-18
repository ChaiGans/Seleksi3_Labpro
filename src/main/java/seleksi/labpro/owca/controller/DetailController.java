package seleksi.labpro.owca.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import seleksi.labpro.owca.dto.FilmDto;
import seleksi.labpro.owca.dto.ReviewDto;
import seleksi.labpro.owca.entity.Film;
import seleksi.labpro.owca.entity.Review;
import seleksi.labpro.owca.entity.User;
import seleksi.labpro.owca.service.FilmService;
import seleksi.labpro.owca.service.ReviewService;
import seleksi.labpro.owca.service.UserService;
import seleksi.labpro.owca.utils.JwtService;
import seleksi.labpro.owca.utils.S3Utils;
import seleksi.labpro.owca.utils.TimeFormatUtil;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/details")
public class DetailController {
    private final FilmService filmService;
    private final JwtService jwtService;
    private final UserService userService;
    private final ReviewService reviewService;

    public DetailController(FilmService filmService, JwtService jwtService, UserService userService, ReviewService reviewService) {
        this.filmService = filmService;
        this.jwtService = jwtService;
        this.userService = userService;
        this.reviewService = reviewService;
    }

    @GetMapping("/film/{id}")
    public String showDetailFilmPage(Model model, HttpServletRequest request, @PathVariable("id") Long id) {

        Optional<Film> film = filmService.getFilmById(id);

        if (film.isEmpty()) {
            return "Film not found.";
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader != null) {
            String token = authorizationHeader.substring(7);

            String userAuthEmail = jwtService.extractUsername(token);

            User loginUser = userService.findByEmail(userAuthEmail);


            model.addAttribute("isBought", filmService.isBought(film.get().getId(), loginUser));
            model.addAttribute("userId", loginUser.getId());
        } else {
            model.addAttribute("isBought", false);
            model.addAttribute("userId", 0);
        }

        film.get().setCoverImageUrl(S3Utils.generatePresignedUrl(film.get().getCoverImageUrl(), filmService.getBucketName(), filmService.getS3Client()));
        film.get().setVideoUrl(S3Utils.generatePresignedUrl(film.get().getVideoUrl(), filmService.getBucketName(), filmService.getS3Client()));

        model.addAttribute("film", film.get());
        model.addAttribute("formattedDuration", TimeFormatUtil.formatDuration(film.get().getDuration()));

        List<Review> allReviews = reviewService.getAllReviewById(film.get().getId());

        List<ReviewDto> getAllReviewDtos = allReviews.stream()
                .map(review -> new ReviewDto(
                        review.getId(),
                        review.getUser().getRealUsername()  ,
                        review.getFilm(),
                        review.getComment(),
                        review.getRating()
                ))
                .collect(Collectors.toList());

        model.addAttribute("reviews", getAllReviewDtos);
        model.addAttribute("currentFilmId", id);
        return "pages/detailfilm.html";
    }
}
