package seleksi.labpro.owca.model.request.ReviewRequest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostReviewRequest {
    private Long filmId;
    private Long userId;
    private String comment;
    private Integer rating;
}
