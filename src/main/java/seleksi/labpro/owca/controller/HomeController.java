package seleksi.labpro.owca.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import seleksi.labpro.owca.entity.User;
import seleksi.labpro.owca.service.UserService;
import seleksi.labpro.owca.utils.JwtService;

@Controller
public class HomeController {
    private final JwtService jwtService;
    private final UserService userService;

    public HomeController(JwtService jwtService, UserService userService) {
        this.jwtService = jwtService;
        this.userService = userService;
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

        return "index.html";
    }
}
