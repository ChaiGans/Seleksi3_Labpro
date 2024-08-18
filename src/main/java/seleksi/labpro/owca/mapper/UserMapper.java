package seleksi.labpro.owca.mapper;

import seleksi.labpro.owca.dto.UserDto;
import seleksi.labpro.owca.entity.User;

public class UserMapper {
    public static UserDto mapToUserDto(User user) {
        return new UserDto(
                user.getId(),
                user.getUsername(),
                user.getPassword(),
                user.getEmail(),
                user.getBalance(),
                user.getOwnedFilms(),
                user.getRole(),
                user.getWishlistFilms(),
                user.getReviews()
        );
    }

    public static User mapToUser(UserDto userDto) {
        return new User(
                userDto.getId(),
                userDto.getPassword(),
                userDto.getUsername(),
                userDto.getEmail(),
                userDto.getBalance(),
                userDto.getOwnedFilms(),
                userDto.getWishlistFilms(),
                userDto.getReviews(),
                userDto.getRole()
        );
    }
}
