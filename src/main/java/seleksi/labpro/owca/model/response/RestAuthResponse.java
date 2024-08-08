package seleksi.labpro.owca.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RestAuthResponse<T> {
    private Status status;
    private String message;
    private T data;
}
