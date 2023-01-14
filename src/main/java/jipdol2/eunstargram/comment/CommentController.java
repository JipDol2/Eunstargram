package jipdol2.eunstargram.comment;

import jipdol2.eunstargram.comment.dto.request.CommentSaveRequestDTO;
import jipdol2.eunstargram.common.EmptyJSON;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
@RequiredArgsConstructor
public class CommentController {

    private CommentSerivce commentSerivce;

    //TODO: 2023/01/14 댓글 업로드 API 생성
    public ResponseEntity<EmptyJSON> uploadComment(CommentSaveRequestDTO commentSaveRequestDTO){

        log.info("comment={}",commentSaveRequestDTO);
        return ResponseEntity.status(HttpStatus.OK).body(commentSerivce.join(commentSaveRequestDTO));
    }
}
