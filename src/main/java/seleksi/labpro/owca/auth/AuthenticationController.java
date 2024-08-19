package seleksi.labpro.owca.auth;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import seleksi.labpro.owca.entity.User;
import seleksi.labpro.owca.model.request.AuthenticationRequest;
import seleksi.labpro.owca.model.request.RegisterRequest;
import seleksi.labpro.owca.model.response.AuthenticationResponse;
import seleksi.labpro.owca.model.response.RestResponse;
import seleksi.labpro.owca.model.response.Status;
import seleksi.labpro.owca.service.UserService;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final UserService userService;
    private final Logger log = LoggerFactory.getLogger(AuthenticationController.class);

    @PostMapping("/register")
    public ResponseEntity<RestResponse<?>> register(
            @RequestBody RegisterRequest request, HttpServletResponse response
            ) {

        User foundUser = userService.findByEmail(request.getEmail());

        User foundUserByUsername = userService.findByUsername(request.getUsername());

        if (foundUser != null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(
                            RestResponse
                                    .builder()
                                    .status(Status.error)
                                    .message("User with that email already exist")
                                    .data(null)
                                    .build()
                    );
        }

        if (foundUserByUsername != null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(
                            RestResponse
                                    .builder()
                                    .status(Status.error)
                                    .message("User with that username already exist")
                                    .data(null)
                                    .build()
                    );
        }

        if (request.getPassword().length() < 8) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(
                            RestResponse
                                    .builder()
                                    .status(Status.error)
                                    .message("Password length must be at least 8 characters.")
                                    .data(null)
                                    .build()
                    );
        }

        AuthenticationResponse authResponse = userService.register(request);

        ResponseCookie cookie = ResponseCookie.from("authToken", authResponse.getToken())
                .httpOnly(true)
                .secure(true)
                .path("/")
                .sameSite("Strict")
                .build();

        return ResponseEntity.ok(
                RestResponse
                        .builder()
                        .status(Status.success)
                        .message("Register successful")
                        .data(authResponse)
                        .build()
        );
    }

    @PostMapping("/login-with-email")
    public ResponseEntity<RestResponse<?>> loginWithEmail(
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
        return ResponseEntity.ok(
                RestResponse
                        .builder()
                        .status(Status.success)
                        .message("Login successful")
                        .data(authResponse)
                        .build()
        );
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
