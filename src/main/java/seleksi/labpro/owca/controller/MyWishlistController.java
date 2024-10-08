package seleksi.labpro.owca.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import seleksi.labpro.owca.dto.FilmDto;
import seleksi.labpro.owca.entity.Film;
import seleksi.labpro.owca.entity.User;
import seleksi.labpro.owca.service.FilmService;
import seleksi.labpro.owca.service.UserService;
import seleksi.labpro.owca.service.WishlistUserService;
import seleksi.labpro.owca.utils.JwtService;
import seleksi.labpro.owca.utils.S3Utils;
import seleksi.labpro.owca.utils.TimeFormatUtil;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class MyWishlistController {

    private final FilmService filmService;
    private final JwtService jwtService;
    private final UserService userService;
    private final WishlistUserService wishlistUserService;

    public MyWishlistController(FilmService filmService, JwtService jwtService, UserService userService, WishlistUserService wishlistUserService) {
        this.filmService = filmService;
        this.jwtService = jwtService;
        this.userService = userService;
        this.wishlistUserService = wishlistUserService;
    }

    @GetMapping("/my-wish-list")
    public String showMyListPage(Model model, HttpServletRequest request, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "4") int size) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }

        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return "redirect:/login";
        }

        String token = authorizationHeader.substring(7);

        String userAuthEmail = jwtService.extractUsername(token);

        User loginUser = userService.findByEmail(userAuthEmail);

        model.addAttribute("currentLoginUser", loginUser);

        List<Film> allFilms = wishlistUserService.getAllWishlist(loginUser.getId());

        int totalFilms = allFilms.size();
        int fromIndex = page * size;
        int toIndex = Math.min(fromIndex + size, totalFilms);

        List<FilmDto> filmDTOs = allFilms.subList(fromIndex, toIndex).stream().map(film -> new FilmDto(
                Math.toIntExact(film.getId()),
                S3Utils.generatePresignedUrl(film.getCoverImageUrl(), filmService.getBucketName(), filmService.getS3Client()),
                film.getDescription(),
                film.getTitle(),
                film.getReleaseYear(),
                TimeFormatUtil.formatDuration(film.getDuration()),
                film.getGenres(),
                film.getDirector(),
                film.getPrice(),
                filmService.isBought(film.getId(), loginUser),
                wishlistUserService.isWishlistedByUserId(film.getId(), loginUser.getId())
        )).collect(Collectors.toList());

        model.addAttribute("films", filmDTOs);
        model.addAttribute("currentPage", page);
        model.addAttribute("size", size);
        model.addAttribute("totalPages", (totalFilms + size - 1) / size);

        return "pages/mywishlist.html";
    }
}
