package seleksi.labpro.owca.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import seleksi.labpro.owca.dto.TopFilmDto;
import seleksi.labpro.owca.entity.Film;
import seleksi.labpro.owca.entity.User;
import seleksi.labpro.owca.service.FilmService;
import seleksi.labpro.owca.service.UserService;
import seleksi.labpro.owca.utils.JwtService;
import seleksi.labpro.owca.utils.TimeFormatUtil;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class HomeController {
    private final JwtService jwtService;
    private final UserService userService;
    private final FilmService filmService;

    public HomeController(JwtService jwtService, UserService userService, FilmService filmService) {
        this.jwtService = jwtService;
        this.userService = userService;
        this.filmService = filmService;
    }

    @GetMapping("/")
    public String showHomePage(Model model, HttpServletRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader != null) {
            String token = authorizationHeader.substring(7);

            String userAuthEmail = jwtService.extractUsername(token);

            User loginUser = userService.findByEmail(userAuthEmail);

            model.addAttribute("username", loginUser.getRealUsername());
            model.addAttribute("balance", loginUser.getBalance());
        }

        List<Film> allFilms = filmService.getAllFilms();

        Collections.sort(allFilms, new Comparator<Film>() {
            @Override
            public int compare(Film f1, Film f2) {
                int count1 = f1.getWishlistedBy().size() + f1.getUsers().size();
                int count2 = f2.getWishlistedBy().size() + f2.getUsers().size();
                return Integer.compare(count2, count1);
            }
        });

        List<Film> allTopBoughtFilms = allFilms.size() > 5 ? allFilms.subList(0, 5) : allFilms;

        List<TopFilmDto> allTopBoughtDtos = allTopBoughtFilms.stream()
                .map(film -> {
                    String description = film.getDescription();
                    if (description.length() > 300) {
                        description = description.substring(0, 300);
                    }
                    return new TopFilmDto(
                            film.getId(),
                            film.getCoverImageUrl(),
                            description,
                            film.getTitle(),
                            film.getReleaseYear(),
                            TimeFormatUtil.formatDuration(film.getDuration()),
                            film.getDirector()
                    );
                })
                .collect(Collectors.toList());
        model.addAttribute("topBoughtFilms", allTopBoughtDtos);

        return "index.html";
    }
}
