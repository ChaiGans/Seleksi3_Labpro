package seleksi.labpro.owca.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import seleksi.labpro.owca.dto.FilmDto;
import seleksi.labpro.owca.entity.Film;
import seleksi.labpro.owca.entity.User;
import seleksi.labpro.owca.model.response.AuthData;
import seleksi.labpro.owca.service.FilmService;
import seleksi.labpro.owca.service.UserService;
import seleksi.labpro.owca.utils.JwtService;
import seleksi.labpro.owca.utils.TimeFormatUtil;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class BrowseController {
    private final FilmService filmService;
    private final JwtService jwtService;
    private final UserService userService;

    public BrowseController(FilmService filmService, JwtService jwtService, UserService userService) {
        this.filmService = filmService;
        this.jwtService = jwtService;
        this.userService = userService;
    }

    @GetMapping("/browse")
    public String showBrowsePage(Model model, HttpServletRequest request) {
        List<Film> allFilms = filmService.getAllFilms();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }

        String authorizationHeader = request.getHeader("Authorization");
        String token = authorizationHeader.substring(7);

        String userAuthEmail = jwtService.extractUsername(token);

        User loginUser = userService.findByEmail(userAuthEmail);

        List<FilmDto> filmDTOs = allFilms.stream().map(film -> new FilmDto(
                Math.toIntExact(film.getId()),
                film.getCoverImageUrl(),
                film.getDescription(),
                film.getTitle(),
                film.getReleaseYear(),
                TimeFormatUtil.formatDuration(film.getDuration()),
                film.getGenres(),
                film.getDirector(),
                film.getPrice(),
                filmService.isBought(film.getId(), loginUser)
        )).collect(Collectors.toList());
        model.addAttribute("films", filmDTOs);
        return "pages/browse.html";
    }
}
