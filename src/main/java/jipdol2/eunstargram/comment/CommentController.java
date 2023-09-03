package jipdol2.eunstargram.comment;

import jipdol2.eunstargram.auth.entity.NoAuth;
import jipdol2.eunstargram.comment.dto.request.CommentSaveRequestDTO;
import jipdol2.eunstargram.comment.dto.response.CommentFindResponseDTO;
import jipdol2.eunstargram.comment.dto.response.ResultComments;
import jipdol2.eunstargram.common.dto.EmptyJSON;
import jipdol2.eunstargram.config.data.UserSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/api/comment")
public class CommentController {

    private final CommentService commentService;

    /** 2023/01/14 댓글 업로드 API 생성 **/
    @PostMapping("/upload")
    public ResponseEntity<EmptyJSON> uploadComment(UserSession userSession,@Valid @RequestBody CommentSaveRequestDTO commentSaveRequestDTO){
        log.info("userSession={},comment={}",userSession.toString(),commentSaveRequestDTO.toString());
        return ResponseEntity.ok().body(commentService.join(userSession.getId(),commentSaveRequestDTO));
    }

    /** 2023/03/11 댓글 전체 조회 API 생성 **/
    @NoAuth
    @GetMapping("/{postId}")
    public ResponseEntity<ResultComments<List<CommentFindResponseDTO>>> findByComments(@PathVariable Long postId){
        log.info("postId={}",postId);
        List<CommentFindResponseDTO> findByAllComments = commentService.findByAllComments(postId);
        return ResponseEntity.ok().body(new ResultComments<>(findByAllComments));
    }
}
