package seleksi.labpro.owca.service.impl;

import org.springframework.stereotype.Service;
import seleksi.labpro.owca.dto.UserDto;
import seleksi.labpro.owca.entity.User;
import seleksi.labpro.owca.mapper.UserMapper;
import seleksi.labpro.owca.respository.UserRepository;
import seleksi.labpro.owca.service.UserService;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<UserDto> getAllUsers() {
        List<User> allUsers = userRepository.findAll();
        return allUsers.stream().map(user -> UserMapper.mapToUserDto(user)).collect(Collectors.toList());

    }

    @Override
    public UserDto createUser(UserDto userDto) {
        User user = UserMapper.mapToUser(userDto);
        User savedUser = userRepository.save(user);
        return UserMapper.mapToUserDto(savedUser);
    }
}
