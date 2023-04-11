package jipdol2.eunstargram.image.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Setter
@Getter
public class ImageRequestDTO {

    private MultipartFile image;

    @Builder
    public ImageRequestDTO(MultipartFile image) {
        this.image = image;
    }
}
