package jipdol2.eunstargram.post;

import jipdol2.eunstargram.post.dto.PostDTO;
import jipdol2.eunstargram.post.dto.request.PostSaveRequestDTO;
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
    @GetMapping("/{memberId}")
    public ResponseEntity<List<PostDTO>> findByAllPosts(@PathVariable("memberId") Long memberSeq){
        log.info("memberSeq={}",memberSeq);
        //TODO: 현재 memberSeq 로 조회하지만 memberId 로 수정 필요
        return ResponseEntity.status(HttpStatus.OK).body(postService.findByAll(memberSeq));
    }

    //TODO: 2023/01/19 게시글 힌간 조회(url 을 어떻게 정의해야되는지)


}
