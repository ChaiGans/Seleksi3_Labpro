package seleksi.labpro.owca.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FilmDto {
    Integer id;
    String coverImageUrl;
    String description;
    String title;
    Integer releaseYear;
    String formattedTime;
    List<String> genres;
    String director;
    Integer price;
    Boolean bought;
}
