package seleksi.labpro.owca.controller.RestController;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import seleksi.labpro.owca.entity.User;
import seleksi.labpro.owca.model.request.UserBalancePostRequest;
import seleksi.labpro.owca.model.response.RestResponse;
import seleksi.labpro.owca.model.response.Status;
import seleksi.labpro.owca.model.response.UserResponse.UserResponse;
import seleksi.labpro.owca.service.UserService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserRestController {
    private final UserService userService;

    @GetMapping
    public ResponseEntity<RestResponse<?>> getUserByQuery(@RequestParam("q") Optional<String> query) {

        List<User> foundUsers = query.isPresent()
                ? userService.findByQueryUsername(query.get())
                : userService.getAllUsers();

        return ResponseEntity.ok(RestResponse
                .builder()
                        .status(Status.success)
                        .message("Get user by query")
                        .data(
                                foundUsers.stream().map((user) -> UserResponse
                                            .builder()
                                            .id(String.valueOf(user.getId()))
                                            .email(user.getEmail())
                                            .username(user.getRealUsername())
                                            .balance(Math.toIntExact(user.getBalance()))
                                            .build()
                                ).collect(Collectors.toList())
                        )
                .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RestResponse<?>> getUserById(@PathVariable("id") Long id) {

        Optional<User> user = userService.findUserById(id);

        if (user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    RestResponse
                            .builder()
                            .status(Status.error)
                            .message("User not found")
                            .data(null)
                            .build()
            );
        }
        return ResponseEntity.ok(RestResponse
                .builder()
                .status(Status.success)
                .message("Get user by query")
                .data(
                        UserResponse
                                .builder()
                                .id(String.valueOf(user.get().getId()))
                                .email(user.get().getEmail())
                                .username(user.get().getRealUsername())
                                .balance(Math.toIntExact(user.get().getBalance()))
                                .build()
                )
                .build());
    }

    @PostMapping("/{id}/balance")
    public ResponseEntity<RestResponse<?>> updateBalance(@PathVariable("id") Long id, @RequestBody UserBalancePostRequest request) {
        User updatedUser = userService.updateUserBalance(id, request.getIncrement());

        return ResponseEntity.ok(RestResponse
                .builder()
                .status(Status.success)
                .message("Update balance user success")
                .data(
                        UserResponse
                                .builder()
                                .id(String.valueOf(updatedUser.getId()))
                                .email(updatedUser.getEmail())
                                .username(updatedUser.getRealUsername())
                                .balance(Math.toIntExact(updatedUser.getBalance()))
                                .build()
                )
                .build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<RestResponse<?>> deleteUserById (@PathVariable("id") Long id) {
        Optional<User> foundUser = userService.findUserById(id);

        if (foundUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    RestResponse
                            .builder()
                            .status(Status.error)
                            .message("User not found")
                            .data(null)
                            .build()
            );
        }

        userService.deleteUserById(id);

        return ResponseEntity.ok(RestResponse
                .builder()
                .status(Status.success)
                .message("Get user by query")
                .data(
                        UserResponse
                                .builder()
                                .id(String.valueOf(foundUser.get().getId()))
                                .email(foundUser.get().getEmail())
                                .username(foundUser.get().getRealUsername())
                                .balance(Math.toIntExact(foundUser.get().getBalance()))
                                .build()
                )
                .build());
    }
}
