package jipdol2.eunstargram.member.dto.request;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter @Setter
public class MemberProfileImageDTO {

    private MultipartFile image;
}
