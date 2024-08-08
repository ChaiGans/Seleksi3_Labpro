package seleksi.labpro.owca.service;

import seleksi.labpro.owca.dto.UserDto;
import seleksi.labpro.owca.entity.User;
import seleksi.labpro.owca.model.request.AuthenticationRequest;
import seleksi.labpro.owca.model.request.RegisterRequest;
import seleksi.labpro.owca.model.response.AuthenticationResponse;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<UserDto> getAllUsers();
    UserDto createUser(UserDto userDto);

    AuthenticationResponse register(RegisterRequest request);

    AuthenticationResponse authenticate(AuthenticationRequest request);

    User findByUsername(String username);
}
