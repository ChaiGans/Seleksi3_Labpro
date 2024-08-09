package seleksi.labpro.owca.model.response.FilmResponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetAllFilmResponse {
    String id;
    String title;
    String director;
    Integer release_year;
    List<String> genre;
    Integer price;
    Integer duration;
    String cover_image_url;
    String created_at;
    String updated_at;
}
