package seleksi.labpro.owca.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import seleksi.labpro.owca.model.request.AuthenticationRequest;
import seleksi.labpro.owca.model.request.RegisterRequest;
import seleksi.labpro.owca.model.response.AuthenticationResponse;
import seleksi.labpro.owca.service.UserService;

@Controller
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody RegisterRequest request
            ) {
        return ResponseEntity.ok(userService.register(request));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody AuthenticationRequest request
            ) {
        return ResponseEntity.ok(userService.authenticate(request));
    }
}
