package jipdol2.eunstargram.post;

import jipdol2.eunstargram.common.dto.EmptyJSON;
import jipdol2.eunstargram.config.data.UserSession;
import jipdol2.eunstargram.post.dto.request.PostEditRequestDTO;
import jipdol2.eunstargram.post.dto.request.PostSaveRequestDTO;
import jipdol2.eunstargram.post.dto.response.PostResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.User;
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

    @ResponseBody
    @GetMapping("/foo")
    public Long foo(UserSession userSession){
        log.info(">>>{}",userSession.id);
        return userSession.id;
    }

    /** 2023/01/10 게시글 업로드 API 생성 **/
    @PostMapping("/upload")
    public ResponseEntity<Long> uploadPost(UserSession userSession, @ModelAttribute PostSaveRequestDTO postDto){
        log.info("articleDTO={}",postDto.toString());
        return ResponseEntity.status(HttpStatus.OK).body(postService.save(postDto));
    }

    /** 2023/01/12 전체 게시글 조회 **/
    @GetMapping("/{memberId}")
    public ResponseEntity<List<PostResponseDTO>> findByAllPosts(
            UserSession userSession,
            @PathVariable("memberId") Long memberId
    ){
        log.info("userSession.id={}",userSession.getId());
        return ResponseEntity.status(HttpStatus.OK).body(postService.findByAll(memberId));
    }
    /** 2023/03/18 한건 게시글 조회 **/
    @GetMapping("/p/{postId}")
    public ResponseEntity<PostResponseDTO> findByPost(
            @PathVariable("postId") Long postId
    ){
        log.info("postId={}",postId);
        return ResponseEntity.status(HttpStatus.OK).body(postService.findByOne(postId));
    }

    /** 2023/02/22 게시글 수정 **/
    @PutMapping("/p/{postId}")
    public ResponseEntity<EmptyJSON> editPost(
            @PathVariable("postId") Long postId,
            @RequestBody PostEditRequestDTO postEditDto
    ){
        log.info("postId={},postEditDto={}",postId,postEditDto.toString());
        return ResponseEntity.status(HttpStatus.OK).body(postService.edit(postId,postEditDto));
    }

    /** 2023/02/24 게시글 삭제 **/
    @PostMapping("/p/delete/{postId}")
    public ResponseEntity<EmptyJSON> deletePost(
            @PathVariable("memberId") Long memberId,
            @PathVariable("postId") Long postId
    ){
        log.info("memberId={},postId={}",memberId,postId);
        return ResponseEntity.status(HttpStatus.OK).body(postService.deletePost(postId));
    }

}
