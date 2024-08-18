package seleksi.labpro.owca.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import seleksi.labpro.owca.entity.Film;
import seleksi.labpro.owca.entity.User;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReviewDto {
    private Long id;
    private String creatorUsername;
    private Film reviewedFilm;
    private String comment;
    private Integer rating;
}
