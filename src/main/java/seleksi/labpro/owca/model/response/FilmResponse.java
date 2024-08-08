package seleksi.labpro.owca.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FilmResponse {
    String id;
    String title;
    String description;
    String director;
    Integer release_year;
    List<String> genre;
    Integer price;
    Integer duration;
    String video_url;
    String cover_image_url;
    String created_at;
    String updated_at;
}
