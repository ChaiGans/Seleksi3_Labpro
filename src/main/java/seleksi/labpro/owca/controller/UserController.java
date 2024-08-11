package seleksi.labpro.owca.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import seleksi.labpro.owca.entity.User;
import seleksi.labpro.owca.service.UserService;

@Controller
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("user", new User());
        return "pages/register.html";
    }
    @GetMapping("/login")
    public String showLoginForm() {
        return "pages/login.html";
    }
}
