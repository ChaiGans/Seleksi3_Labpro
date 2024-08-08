package seleksi.labpro.owca.auth;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import seleksi.labpro.owca.model.request.AuthenticationRequest;
import seleksi.labpro.owca.model.request.RegisterRequest;
import seleksi.labpro.owca.model.response.AuthenticationResponse;
import seleksi.labpro.owca.service.UserService;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final UserService userService;
    private final Logger log = LoggerFactory.getLogger(AuthenticationController.class);

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody RegisterRequest request, HttpServletResponse response
            ) {
        AuthenticationResponse authResponse = userService.register(request);

        ResponseCookie cookie = ResponseCookie.from("authToken", authResponse.getToken())
                .httpOnly(true)
                .secure(true)
                .path("/")
                .sameSite("Strict")
                .build();

        return ResponseEntity.ok(authResponse);
    }

    @PostMapping("/login-with-email")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody AuthenticationRequest request, HttpServletResponse response
            ) {

        AuthenticationResponse authResponse = userService.authenticate(request);

        ResponseCookie cookie = ResponseCookie.from("authToken", authResponse.getToken())
                .httpOnly(true)
                .secure(true)
                .path("/")
                .sameSite("Strict")
                .build();
        response.addHeader("Set-Cookie", cookie.toString());
        return ResponseEntity.ok(authResponse);
    }

    @PostMapping("/logout")
    public String logout(HttpServletResponse response) {
        ResponseCookie cookie = ResponseCookie.from("authToken", null)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(0)
                .sameSite("Strict")
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        // Return a success message
        Map<String, String> result = new HashMap<>();
        result.put("message", "Logged out successfully");
        return "index.html";
    }


}
