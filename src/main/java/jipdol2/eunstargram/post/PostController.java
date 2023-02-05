package jipdol2.eunstargram.post;

import jipdol2.eunstargram.image.entity.Image;
import jipdol2.eunstargram.post.dto.response.PostResponseDTO;
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

    /** 2023/01/10 게시글 업로드 API 생성 **/
    @PostMapping("/upload")
    public ResponseEntity<EmptyJSON> uploadPost(@RequestBody PostSaveRequestDTO postDto){
        log.info("articleDTO={}",postDto.toString());
        return ResponseEntity.status(HttpStatus.OK).body(postService.save(postDto));
    }

    /** 2023/01/12 전체 게시글 조회 **/
    @GetMapping("/{memberId}")
    public ResponseEntity<List<PostResponseDTO>> findByAllPosts(@PathVariable("memberId") Long memberSeq){
        log.info("memberSeq={}",memberSeq);
        //TODO: 현재 memberSeq 로 조회하지만 memberId 로 수정 필요
        return ResponseEntity.status(HttpStatus.OK).body(postService.findByAll(memberSeq));
    }

    /** 2023/02/02 포스팅 글 전체 가져오기 **/
    @GetMapping("/{memberId}/postImage")
    public ResponseEntity<List<Image>> findByPostImage(@PathVariable("memberId") String memberId){
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    //TODO: 2023/01/19 게시글 힌건 조회(url 을 어떻게 정의해야되는지)

    //TODO: 2023/02/05 게시글 이미지 전체 조회

}
