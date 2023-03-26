package jipdol2.eunstargram.image.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Setter
@Getter
public class ImageRequestDTO {

    private Long memberId;

    private MultipartFile image;

    @Builder
    public ImageRequestDTO(Long memberId, MultipartFile image) {
        this.memberId = memberId;
        this.image = image;
    }
}
