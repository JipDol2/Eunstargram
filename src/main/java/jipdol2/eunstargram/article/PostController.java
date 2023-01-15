package jipdol2.eunstargram.article;

import jipdol2.eunstargram.article.dto.PostDTO;
import jipdol2.eunstargram.article.dto.request.PostSaveRequestDTO;
import jipdol2.eunstargram.common.dto.EmptyJSON;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/api/post")
public class PostController {

    private final PostService postService;

    //TODO: 2023/01/10 게시글 업로드 API 생성
    @PostMapping("/upload")
    public ResponseEntity<EmptyJSON> uploadPost(@RequestBody PostSaveRequestDTO postDto){

        log.info("articleDTO={}",postDto.toString());
        return ResponseEntity.status(HttpStatus.OK).body(postService.save(postDto));
    }

    //TODO: 2023/01/12 전체 게시글 조회
    @GetMapping("/all-posts/{id}")
    public ResponseEntity<List<PostDTO>> findAllArticles(@PathVariable("id") Long id){
        log.info("memberId={}",id);
        return ResponseEntity.status(HttpStatus.OK).body(postService.findAll(id));
    }
}
