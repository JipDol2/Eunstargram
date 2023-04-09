package jipdol2.eunstargram.image;

import jipdol2.eunstargram.image.entity.ImageJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ImageService {

    //TODO: 후에 AWS(S3) 로 교체해야 함
    @Value("${com.upload.path}")
    private String uploadPath;

    private final ImageJpaRepository postImageJpaRepository;

    public String uploadImage(MultipartFile imageDTO){

        /**
         * 이미지 파일 저장 방법
         * - https://workshop-6349.tistory.com/entry/Spring-Boot-%ED%8C%8C%EC%9D%BC%EC%9D%B4%EB%AF%B8%EC%A7%80-%EC%97%85%EB%A1%9C%EB%93%9C%ED%95%98%EA%B8%B0
         * - https://velog.io/@alswl689/SpringBoot-with-JPA-%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8MN-4.%EC%9D%B4%EB%AF%B8%EC%A7%80-%EC%97%85%EB%A1%9C%EB%93%9C%EC%8D%B8%EB%84%A4%EC%9D%BC%EC%9D%B4%EB%AF%B8%EC%A7%80%EC%82%AD%EC%A0%9C
         */

        //디렉토리 생성
        String toDay = String.valueOf(LocalDate.now().getYear());
        String folderPath = (uploadPath + "upload/" + toDay).replace("/", File.separator);
        File folder = new File(folderPath);

        if(!folder.exists()){
            folder.mkdirs();
        }

        String uuid = UUID.randomUUID().toString();
        String imageName = uuid + "_" + imageDTO.getOriginalFilename();
        String saveName = folderPath + File.separator + imageName;
        Path path = Paths.get(saveName);

        try{
            imageDTO.transferTo(path);
        }catch (Exception e){
            e.printStackTrace();
        }

        return imageName;
    }
}
