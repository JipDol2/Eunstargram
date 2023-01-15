package jipdol2.eunstargram.comment;

import jipdol2.eunstargram.comment.dto.request.CommentSaveRequestDTO;
import jipdol2.eunstargram.common.dto.EmptyJSON;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/api/comment")
public class CommentController {

    private final CommentSerivce commentSerivce;

    //TODO: 2023/01/14 댓글 업로드 API 생성
    @PostMapping("/upload")
    public ResponseEntity<EmptyJSON> uploadComment(@RequestBody CommentSaveRequestDTO commentSaveRequestDTO){

        log.info("comment={}",commentSaveRequestDTO);
        return ResponseEntity.status(HttpStatus.OK).body(commentSerivce.join(commentSaveRequestDTO));
    }
}
