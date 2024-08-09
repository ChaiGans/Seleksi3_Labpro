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
public class DeleteFilmResponse {
    String id;
    String title;
    String description;
    String director;
    Integer release_year;
    List<String> genre;
    String video_url;
    String created_at;
    String updated_at;
}
