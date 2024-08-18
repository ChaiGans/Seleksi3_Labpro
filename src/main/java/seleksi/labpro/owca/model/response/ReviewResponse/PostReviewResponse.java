package seleksi.labpro.owca.model.response.ReviewResponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostReviewResponse {
    private Long id;
    private String comment;
    private Integer rating;
    private Long filmId;
    private Long userId;
}
