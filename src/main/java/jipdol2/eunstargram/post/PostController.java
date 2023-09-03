package jipdol2.eunstargram.post;

import jipdol2.eunstargram.auth.entity.NoAuth;
import jipdol2.eunstargram.common.dto.EmptyJSON;
import jipdol2.eunstargram.post.dto.response.ResultPosts;
import jipdol2.eunstargram.config.data.UserSession;
import jipdol2.eunstargram.post.dto.request.PostEditRequestDTO;
import jipdol2.eunstargram.post.dto.request.PostSaveRequestDTO;
import jipdol2.eunstargram.post.dto.response.PostResponseDTO;
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
    public ResponseEntity<Long> uploadPost(UserSession userSession, @ModelAttribute PostSaveRequestDTO postDto){
        postDto.validate();
        log.info("articleDTO={}",postDto.toString());
        return ResponseEntity.ok().body(postService.save(userSession.getId(),postDto));
    }

    /** 2023/01/12 전체 게시글 조회 **/
    @NoAuth
    @GetMapping("/{nickname}")
    public ResponseEntity<ResultPosts<List<PostResponseDTO>>> findByAllPosts(
            @PathVariable String nickname
    ){
        log.info("nickname={}",nickname);
        List<PostResponseDTO> findByPosts = postService.findByAll(nickname);
        return ResponseEntity.ok().body(new ResultPosts<>(findByPosts));
    }
    /** 2023/03/18 한건 게시글 조회 **/
    @GetMapping("/p/{postId}")
    public ResponseEntity<PostResponseDTO> findByPost(
            @PathVariable("postId") Long postId
    ){
        log.info("postId={}",postId);
        return ResponseEntity.ok().body(postService.findByOne(postId));
    }

    /** 2023/02/22 게시글 수정 **/
    @PutMapping("/p/{postId}")
    public ResponseEntity<EmptyJSON> editPost(
            @PathVariable("postId") Long postId,
            @RequestBody PostEditRequestDTO postEditDto
    ){
        log.info("postId={},postEditDto={}",postId,postEditDto.toString());
        return ResponseEntity.ok().body(postService.edit(postId,postEditDto));
    }

    /** 2023/02/24 게시글 삭제 **/
    @DeleteMapping("/p/delete/{postId}")
    public ResponseEntity<EmptyJSON> deletePost(
            UserSession userSession,
            @PathVariable("postId") Long postId
    ){
        log.info("userSession={},postId={}",userSession.toString(),postId);
        return ResponseEntity.ok().body(postService.deletePost(userSession.getId(),postId));
    }

}
