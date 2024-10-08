package seleksi.labpro.owca.service.impl;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import seleksi.labpro.owca.dto.UserDto;
import seleksi.labpro.owca.entity.Role;
import seleksi.labpro.owca.entity.User;
import seleksi.labpro.owca.mapper.UserMapper;
import seleksi.labpro.owca.model.request.AuthenticationRequest;
import seleksi.labpro.owca.model.request.RegisterRequest;
import seleksi.labpro.owca.model.response.AuthenticationResponse;
import seleksi.labpro.owca.respository.ReviewRepository;
import seleksi.labpro.owca.respository.UserRepository;
import seleksi.labpro.owca.service.UserService;
import seleksi.labpro.owca.utils.JwtService;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final ReviewRepository reviewRepository;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService, AuthenticationManager authenticationManager, ReviewRepository reviewRepository, ReviewRepository reviewRepository1) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.reviewRepository = reviewRepository1;
    }

    @Override
    public List<User> getAllUsers() {
        List<User> allUsers = userRepository.findAll();
        return allUsers;

    }

    @Override
    public UserDto createUser(UserDto userDto) {
        User user = UserMapper.mapToUser(userDto);
        User savedUser = userRepository.save(user);
        return UserMapper.mapToUserDto(savedUser);
    }

    @Override
    public AuthenticationResponse register(RegisterRequest request) {
        var newUser = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .balance(0L)
                .role(Role.USER)
                .build();
        userRepository.save(newUser);
        var jwtToken = jwtService.generateToken(newUser);
        return AuthenticationResponse
                .builder()
                .token(jwtToken)
                .build();
    }

    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        Optional<User> foundUsername = userRepository.findByUsername(request.getEmail());

        String userEmail = foundUsername.isEmpty() ? request.getEmail() : foundUsername.get().getEmail();
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        userEmail,
                        request.getPassword()
                )
        );
        var user = userRepository.findByEmail(userEmail).orElseThrow();
        var jwtToken = jwtService.generateToken(user);

        return AuthenticationResponse.builder().token(jwtToken).build();
    }



    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    @Override
    public List<User> findByQueryUsername(String query) {
        List<User> allUser = userRepository.findUsersByUsernameStartingWith(query);

        if (allUser.isEmpty()) {
            allUser = userRepository.findAll();
        }

        return allUser;
    }

    @Override
    public Optional<User> findUserById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public void deleteUserById(Long id) {
        Optional<User> user = userRepository.findById(id);

        if (user.isPresent()) {
            user.get().getReviews().forEach(review -> reviewRepository.deleteById(review.getId()));
            userRepository.deleteById(user.get().getId());
        }
    }

    @Override
    public User updateUserBalance(Long id, Integer newBalance) {
        return userRepository.findById(id)
                .map(user -> {
                    user.setBalance(user.getBalance() + Long.valueOf(newBalance));
                    return userRepository.save(user);
                })
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));
    }
}
