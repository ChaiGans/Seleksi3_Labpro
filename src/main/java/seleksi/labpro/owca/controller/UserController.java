package seleksi.labpro.owca.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import seleksi.labpro.owca.dto.UserDto;
import seleksi.labpro.owca.entity.User;
import seleksi.labpro.owca.service.UserService;

import java.util.List;

@Controller
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("user", new User());
        return "pages/register.html";
    }

    @PostMapping("/register")
    public String processRegistration(User user) {
        // Save user or handle registration
        return "redirect:/pages/login/html";
    }

    @GetMapping("/login")
    public String showLoginForm() {
        return "pages/login.html";
    }
    
    @PostMapping("/")
    public ResponseEntity<UserDto> createNewUser(@RequestBody UserDto userDto) {
        UserDto savedDto = userService.createUser(userDto);
        return new ResponseEntity<>(savedDto, HttpStatus.CREATED);
    }
}
