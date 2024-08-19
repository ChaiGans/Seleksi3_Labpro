package seleksi.labpro.owca.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TopFilmDto {
    Long id;
    String coverImageUrl;
    String description;
    String title;
    Integer releaseYear;
    String formattedTime;
    String director;
}
