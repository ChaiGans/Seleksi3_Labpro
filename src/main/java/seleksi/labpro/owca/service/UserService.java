package seleksi.labpro.owca.service;

import seleksi.labpro.owca.dto.UserDto;

import java.util.List;

public interface UserService {
    List<UserDto> getAllUsers();
    UserDto createUser(UserDto userDto);
}
