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
import seleksi.labpro.owca.utils.JwtService;
import seleksi.labpro.owca.utils.TimeFormatUtil;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class MyListController {
    private final FilmService filmService;
    private final JwtService jwtService;
    private final UserService userService;

    public MyListController(FilmService filmService, JwtService jwtService, UserService userService) {
        this.filmService = filmService;
        this.jwtService = jwtService;
        this.userService = userService;
    }

    @GetMapping("/my-list")
    public String showMyListPage(Model model, HttpServletRequest request, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "4") int size) {
        List<Film> allFilms = filmService.getAllFilms();
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

        List<Film> boughtFilms = allFilms.stream()
                .filter(film -> filmService.isBought(film.getId(), loginUser))
                .collect(Collectors.toList());

        int totalFilms = boughtFilms.size();
        int fromIndex = page * size;
        int toIndex = Math.min(fromIndex + size, totalFilms);

        List<FilmDto> filmDTOs = boughtFilms.subList(fromIndex, toIndex).stream().map(film -> new FilmDto(
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
        model.addAttribute("currentPage", page);
        model.addAttribute("size", size);
        model.addAttribute("totalPages", (totalFilms + size - 1) / size);

        return "pages/mylist.html";
    }
}
