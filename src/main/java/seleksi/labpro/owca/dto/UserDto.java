package seleksi.labpro.owca.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import seleksi.labpro.owca.entity.Film;
import seleksi.labpro.owca.entity.Review;
import seleksi.labpro.owca.entity.Role;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private Long id;
    private String username;
    private String password;
    private String email;
    private Long balance;
    private List<Film> ownedFilms;
    private Role role;
    private Set<Film> wishlistFilms;
    private Set<Review> reviews;

    @Override
    public String toString() {
        return "UserDto{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", balance=' " + balance + '\'' +
                '}';
    }
}