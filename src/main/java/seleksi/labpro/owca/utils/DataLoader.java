package seleksi.labpro.owca.utils;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import seleksi.labpro.owca.entity.Role;
import seleksi.labpro.owca.entity.User;
import seleksi.labpro.owca.respository.UserRepository;

@Component
public class DataLoader implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataLoader(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        // Check if admin user already exists
        if (userRepository.findByUsername("admin").isEmpty()) {
            // Create a new admin user
            var admin = User.builder()
                    .username("admin")
                    .password(passwordEncoder.encode("admin123")) // Securely encode the password
                    .email("admin@gmail.com")
                    .balance(0L)
                    .role(Role.ADMIN)
                    .build();

            userRepository.save(admin);
        }

        if (userRepository.findByUsername("chaigans").isEmpty()) {
            // Ordinary user
            var user = User
                    .builder()
                    .username("chaigans")
                    .password(passwordEncoder.encode("chaigans"))
                    .email("elbertchailes888@gmail.com")
                    .balance(0L)
                    .role(Role.USER)
                    .build();

            userRepository.save(user);
        }
    }
}
