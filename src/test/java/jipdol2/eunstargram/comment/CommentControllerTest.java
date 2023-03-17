package jipdol2.eunstargram.comment;

import com.fasterxml.jackson.databind.ObjectMapper;
import jipdol2.eunstargram.auth.entity.Session;
import jipdol2.eunstargram.auth.entity.SessionJpaRepository;
import jipdol2.eunstargram.comment.dto.request.CommentSaveRequestDTO;
import jipdol2.eunstargram.comment.entity.Comment;
import jipdol2.eunstargram.comment.entity.CommentRepository;
import jipdol2.eunstargram.exception.PostNotFound;
import jipdol2.eunstargram.member.entity.Member;
import jipdol2.eunstargram.member.entity.MemberRepository;
import jipdol2.eunstargram.post.entity.Post;
import jipdol2.eunstargram.post.entity.PostRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.Cookie;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class CommentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private SessionJpaRepository sessionJpaRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private final String COMMON_URL = "/api/comment";

    @Test
    @DisplayName("댓글 저장 : /api/comment/upload 요청시 200 status code 리턴")
    @Transactional
    void commentSaveTest() throws Exception {

        //given
        Member member = Member.builder()
                .memberEmail("jipdol2@gmail.com")
                .password("1234")
                .nickname("jipdol2")
                .intro("im jipdol2")
                .phoneNumber("010-1234-5678")
                .birthDay("1999-01-01")
                .build();

        Long memberId = memberRepository.save(member);

        Post post = Post.builder()
                .content("can i have a lunch with me?")
                .deleteYn("N")
                .likeNumber(0L)
                .member(member)
                .build();

        Long postId = postRepository.save(post);

        CommentSaveRequestDTO commentDto = CommentSaveRequestDTO.builder()
                .content("already i had lunch")
                .postId(postId)
                .build();

        Session session = member.addSession();
        Cookie cookie = new Cookie("SESSION",session.getAccessToken());

        String json = objectMapper.writeValueAsString(commentDto);

        //expect
        mockMvc.perform(post(COMMON_URL+"/upload")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .cookie(cookie)
                )
                .andExpect(status().isOk())
                .andDo(print());

        Post findByPost = postRepository.findByOne(postId)
                .orElseThrow(() -> new PostNotFound());

        List<Comment> comments = findByPost.getComments();

        //then
        assertThat(comments.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("댓글 전체 조회 : /api/comment/{postId} 요청시 200 status code 리턴 및 댓글 조회")
    @Transactional
    void commentFindAllTest() throws Exception{
        //given
        Member member = Member.builder()
                .memberEmail("jipdol2@gmail.com")
                .password("1234")
                .nickname("jipdol2")
                .intro("im jipdol2")
                .phoneNumber("010-1234-5678")
                .birthDay("1999-01-01")
                .build();

        Long memberId = memberRepository.save(member);

        Post post = Post.builder()
                .content("can i have a lunch with me?")
                .deleteYn("N")
                .likeNumber(0L)
                .member(member)
                .build();

        Long postId = postRepository.save(post);

        Comment comment1 = Comment.builder()
                .content("already i had lunch")
                .likeNumber(0L)
                .deleteYn("N")
                .post(post)
                .member(member)
                .build();

        Comment comment2 = Comment.builder()
                .content("i dont have lunch")
                .likeNumber(0L)
                .deleteYn("N")
                .post(post)
                .member(member)
                .build();

        commentRepository.save(comment1);
        commentRepository.save(comment2);

        //when
        mockMvc.perform(get(COMMON_URL+"/{postId}",postId))
                .andExpect(status().isOk())
                .andDo(print());

        Post findByPost = postRepository.findByOne(postId)
                .orElseThrow(() -> new PostNotFound());
        //then
        assertThat(findByPost.getComments().size()).isEqualTo(2);
    }
}