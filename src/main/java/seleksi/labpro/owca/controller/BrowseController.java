package seleksi.labpro.owca.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import seleksi.labpro.owca.dto.FilmDto;
import seleksi.labpro.owca.entity.Film;
import seleksi.labpro.owca.entity.User;
import seleksi.labpro.owca.model.response.AuthData;
import seleksi.labpro.owca.service.FilmService;
import seleksi.labpro.owca.service.UserService;
import seleksi.labpro.owca.utils.JwtService;
import seleksi.labpro.owca.utils.S3Utils;
import seleksi.labpro.owca.utils.TimeFormatUtil;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/browse")
public class BrowseController {
    private final FilmService filmService;
    private final JwtService jwtService;
    private final UserService userService;

    public BrowseController(FilmService filmService, JwtService jwtService, UserService userService) {
        this.filmService = filmService;
        this.jwtService = jwtService;
        this.userService = userService;
    }

    @GetMapping
    public String showBrowsePage(Model model, HttpServletRequest request, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "4") int size, @RequestParam(value = "q", required = false) Optional<String> query) {
        List<Film> allFilms = query.isPresent()
                ? filmService.findByQueryTitle(query.get())
                : filmService.getAllFilms();

        if (query.isPresent()) {
            allFilms.forEach(film -> film.setCoverImageUrl(S3Utils.generatePresignedUrl(
                    film.getCoverImageUrl(),
                    filmService.getBucketName(),
                    filmService.getS3Client()
            )));
            model.addAttribute("query", query.get());
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String authorizationHeader = request.getHeader("Authorization");

        int totalFilms = allFilms.size();
        int fromIndex = page * size;
        int toIndex = Math.min(fromIndex + size, totalFilms);

        if (authorizationHeader != null) {
            String token = authorizationHeader.substring(7);

            String userAuthEmail = jwtService.extractUsername(token);

            User loginUser = userService.findByEmail(userAuthEmail);

            List<Film> sortedFilms = allFilms.stream()
                    .sorted(Comparator.comparing(film -> filmService.isBought(film.getId(), loginUser)))
                    .collect(Collectors.toList());

            List<FilmDto> filmDTOs = sortedFilms.subList(fromIndex, toIndex).stream().map(film -> new FilmDto(
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

            model.addAttribute("balance", loginUser.getBalance());

        } else {
            List<FilmDto> filmDTOs = allFilms.subList(fromIndex, toIndex).stream().map(film -> new FilmDto(
                    Math.toIntExact(film.getId()),
                    film.getCoverImageUrl(),
                    film.getDescription(),
                    film.getTitle(),
                    film.getReleaseYear(),
                    TimeFormatUtil.formatDuration(film.getDuration()),
                    film.getGenres(),
                    film.getDirector(), 
                    film.getPrice(),
                    true
            )).collect(Collectors.toList());
            model.addAttribute("films", filmDTOs);
        }

        model.addAttribute("currentPage", page);
        model.addAttribute("size", size);
        model.addAttribute("totalPages", (totalFilms + size - 1) / size);
        return "pages/browse.html";
    }
}
