package jipdol2.eunstargram.post.dto.request;

import jipdol2.eunstargram.exception.ImageFileArgumentNotValidation;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Setter
@Getter
@ToString
public class PostSaveRequestDTO {

    private String content;

    private MultipartFile image;

    /**
     * json 형태의 데이터를 객체형태로 변환하는데 JackSon Library 가 사용된다.
     * 이때, 객체에는 '기본 생성자' 가 반드시 존재해야한다.
     * 이유는 JackSon Library가 기본생성자를 이용하여 객체로 변환시켜주기 때문이다. => 역직렬화
     */
    public PostSaveRequestDTO(){
    }

    @Builder
    public PostSaveRequestDTO(String content, MultipartFile image) {
        this.content = content;
        this.image = image;
    }

    public void validate(){
        if(image.isEmpty()){
            throw new ImageFileArgumentNotValidation("image","이미지 파일은 필수입니다.");
        }
    }
}
