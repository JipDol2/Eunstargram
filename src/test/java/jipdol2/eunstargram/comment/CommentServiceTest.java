package jipdol2.eunstargram.comment;

import jipdol2.eunstargram.comment.dto.request.CommentSaveRequestDTO;
import jipdol2.eunstargram.comment.entity.Comment;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class CommentServiceTest {

    //Service
    @Autowired private CommentService commentService;

    @Test
    @DisplayName("댓글 저장 테스트")
    void saveCommentTest() throws Exception{

        //given

        //when

        //then
    }

    @Test
    @DisplayName("댓글 조회 테스트")
    void findByComments() throws Exception{

        //given

        //when

        //then
    }

    private CommentSaveRequestDTO createCommentSaveRequestDTO(String content,Long postId){
        CommentSaveRequestDTO commentDTO = CommentSaveRequestDTO.builder()
                .content(content)
                .postId(postId)
                .build();
        return commentDTO;
    }
}