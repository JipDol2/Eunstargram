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
    public ResponseEntity<Long> uploadPost(@ModelAttribute PostSaveRequestDTO postDto){
        log.info("articleDTO={}",postDto.toString());
        return ResponseEntity.status(HttpStatus.OK).body(postService.save(postDto));
    }

    /** 2023/01/12 전체 게시글 조회 **/
    @GetMapping("/{memberId}")
    public ResponseEntity<List<PostResponseDTO>> findByAllPosts(@PathVariable("memberId") Long memberId){
        log.info("memberId={}",memberId);
        //TODO: 현재 memberSeq 로 조회하지만 memberId 로 수정 필요
        return ResponseEntity.status(HttpStatus.OK).body(postService.findByAll(memberId));
    }

    //TODO: 게시글 수정 api
    //TODO: RequestDto 정의한다음에 edit 함수에 인자 넘겨주기
    //TODO: 호돌맨 영상 보고 PostEdit class 만들어보기
    //TODO: test code 작성
    @PutMapping("/{memberId}/{postId}")
    public ResponseEntity<?> editPost(@PathVariable("memberId") Long memberId,@PathVariable("postId") Long postId){
        log.info("memberId={},postId={}",memberId,postId);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    //TODO : 게시글 삭제 api

}
