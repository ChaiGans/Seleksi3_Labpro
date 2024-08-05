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
import seleksi.labpro.owca.service.UserService;

import java.util.List;

@Controller
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/products")
    public String showUsers(Model model) {
        try {
            List<UserDto> users = userService.getAllUsers();
            if (users.isEmpty()) {
                log.info("No users found");
            } else {
                log.info("Number of users found: {}", users.size());
                log.info("First user: {}", users.get(0));
            }
            model.addAttribute("users", users);
        } catch (Exception e) {
            log.error("Error retrieving users", e);
        }
        return "users/index.html";
    }

    @PostMapping("/")
    public ResponseEntity<UserDto> createNewUser(@RequestBody UserDto userDto) {
        UserDto savedDto = userService.createUser(userDto);
        return new ResponseEntity<>(savedDto, HttpStatus.CREATED);
    }
}
