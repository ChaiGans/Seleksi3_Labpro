package seleksi.labpro.owca.auth;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import seleksi.labpro.owca.model.request.AuthenticationRequest;
import seleksi.labpro.owca.model.request.RestAuthRequest;
import seleksi.labpro.owca.model.response.AuthData;
import seleksi.labpro.owca.model.response.AuthenticationResponse;
import seleksi.labpro.owca.model.response.RestAuthResponse;
import seleksi.labpro.owca.model.response.Status;
import seleksi.labpro.owca.service.UserService;
import seleksi.labpro.owca.utils.JwtService;

import java.nio.file.attribute.UserPrincipal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class RestAuthController {
    private final UserService userService;
    private final UserDetailsService userDetailsService;
    private final JwtService jwtService;

    @PostMapping("/login")
    public ResponseEntity<RestAuthResponse> loginWithUsername(
            @RequestBody RestAuthRequest request, HttpServletResponse response
    ) {
        var user = userService.findByUsername(request.getUsername());

        AuthenticationRequest authRequest = new AuthenticationRequest(user.getEmail(), request.getPassword());

        AuthenticationResponse authResponse = userService.authenticate(authRequest);

        List<HashMap<String, String>> role = jwtService.extractRoles(authResponse.getToken());

        if (!role.getFirst().get("authority").equals("ADMIN")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                RestAuthResponse
                        .builder()
                        .status(Status.error)
                        .message("User credentials does not match.")
                        .data(null)
                        .build()
            );
        }

        AuthData authData = AuthData.builder()
                .username(request.getUsername())
                .token(authResponse.getToken())
                .build();

        RestAuthResponse restAuthResponse = RestAuthResponse.builder()
                .status(Status.success)
                .message("Login successful.")
                .data(authData)
                .build();

        ResponseCookie cookie = ResponseCookie.from("authToken", authResponse.getToken())
                .httpOnly(true)
                .secure(true)
                .path("/")
                .sameSite("Strict")
                .build();
        response.addHeader("Set-Cookie", cookie.toString());
        return ResponseEntity.ok(restAuthResponse);
    }

    @GetMapping("/self")
    public ResponseEntity<RestAuthResponse> getSelfDetails(HttpServletRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.ok(RestAuthResponse.builder()
                    .status(Status.error)
                    .message("User not authenticated")
                    .build());
        }

        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);

            AuthData authData = AuthData
                    .builder()
                    .token(token)
                    .username(jwtService.extractUsername(token))
                    .build();

            return ResponseEntity.ok(RestAuthResponse.builder()
                    .status(Status.success)
                    .message("User details retrieved successfully.")
                    .data(authData)
                    .build());
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                RestAuthResponse
                        .builder()
                        .status(Status.error)
                        .message("Fail to get self")
                        .data(null)
                        .build()
        );
    }
}
