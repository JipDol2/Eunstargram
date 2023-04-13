package jipdol2.eunstargram.comment;

import com.fasterxml.jackson.databind.ObjectMapper;
import jipdol2.eunstargram.auth.entity.Session;
import jipdol2.eunstargram.auth.entity.SessionJpaRepository;
import jipdol2.eunstargram.comment.dto.request.CommentSaveRequestDTO;
import jipdol2.eunstargram.comment.entity.Comment;
import jipdol2.eunstargram.comment.entity.CommentRepository;
import jipdol2.eunstargram.exception.PostNotFound;
import jipdol2.eunstargram.jwt.JwtManager;
import jipdol2.eunstargram.jwt.dto.UserSessionDTO;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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
    private ObjectMapper objectMapper;

    @Autowired
    private JwtManager jwtManager;

    private final String COMMON_URL = "/api/comment";

    @Test
    @DisplayName("댓글 저장 : /api/comment/upload 요청시 200 status code 리턴")
    @Transactional
    void commentSaveTest() throws Exception {

        //given
        Member member = createMember(
                "jipdol2@gmail.com",
                "1234",
                "jipdol2",
                "im jipdol2",
                "010-1234-5678",
                "1999-01-01");

        Long memberId = memberRepository.save(member);

        Post post = createPost(member,"can i have a lunch with me?");

        Long postId = postRepository.save(post);

        CommentSaveRequestDTO commentDto = CommentSaveRequestDTO.builder()
                .content("already i had lunch")
                .postId(postId)
                .build();

//        Session session = member.addSession();
        UserSessionDTO sessionDTO = UserSessionDTO.builder()
                .id(member.getId())
                .email(member.getMemberEmail())
                .nickname(member.getNickname())
                .build();
        String accessToken = jwtManager.makeToken(sessionDTO, "ACCESS");
        Cookie cookie = new Cookie("SESSION",accessToken);

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
    @DisplayName("댓글 저장 : 댓글 내용은 필수입니다")
    @Transactional
    void commentSaveContentTest() throws Exception {

        //given
        Member member = createMember(
                "jipdol2@gmail.com",
                "1234",
                "jipdol2",
                "im jipdol2",
                "010-1234-5678",
                "1999-01-01");

        Long memberId = memberRepository.save(member);

        Post post = createPost(member,"can i have a lunch with me?");

        Long postId = postRepository.save(post);

        CommentSaveRequestDTO commentDto = CommentSaveRequestDTO.builder()
                .content("  ")
                .postId(postId)
                .build();

//        Session session = member.addSession();
        UserSessionDTO sessionDTO = UserSessionDTO.builder()
                .id(member.getId())
                .email(member.getMemberEmail())
                .nickname(member.getNickname())
                .build();
        String accessToken = jwtManager.makeToken(sessionDTO, "ACCESS");
        Cookie cookie = new Cookie("SESSION",accessToken);

        String json = objectMapper.writeValueAsString(commentDto);

        //expect
        mockMvc.perform(post(COMMON_URL+"/upload")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .cookie(cookie)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.message").value("Bad Request"))
                .andExpect(jsonPath("$.validation.content").value("댓글을 입력해주세요"))
                .andDo(print());
    }

    @Test
    @DisplayName("댓글 저장 : 댓글 내용은 필수입니다")
    @Transactional
    void commentSavePostIdTest() throws Exception {

        //given
        Member member = createMember(
                "jipdol2@gmail.com",
                "1234",
                "jipdol2",
                "im jipdol2",
                "010-1234-5678",
                "1999-01-01");

        Long memberId = memberRepository.save(member);

        Post post = createPost(member,"can i have a lunch with me?");

        Long postId = postRepository.save(post);

        CommentSaveRequestDTO commentDto = CommentSaveRequestDTO.builder()
                .content("can i have a lunch with me?")
                .postId(null)
                .build();

//        Session session = member.addSession();
        UserSessionDTO sessionDTO = UserSessionDTO.builder()
                .id(member.getId())
                .email(member.getMemberEmail())
                .nickname(member.getNickname())
                .build();
        String accessToken = jwtManager.makeToken(sessionDTO, "ACCESS");
        Cookie cookie = new Cookie("SESSION",accessToken);

        String json = objectMapper.writeValueAsString(commentDto);

        //expect
        mockMvc.perform(post(COMMON_URL+"/upload")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .cookie(cookie)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.message").value("Bad Request"))
                .andExpect(jsonPath("$.validation.postId").value("postId 는 null 일 수 없습니다"))
                .andDo(print());
    }

    @Test
    @DisplayName("댓글 전체 조회 : /api/comment/{postId} 요청시 200 status code 리턴 및 댓글 조회")
    @Transactional
    void commentFindAllTest() throws Exception{
        //given
        Member member = createMember(
                "jipdol2@gmail.com",
                "1234",
                "jipdol2",
                "im jipdol2",
                "010-1234-5678",
                "1999-01-01");

        Long memberId = memberRepository.save(member);

        Post post = createPost(member,"No,i don't enough time sorry friend!");

        Long postId = postRepository.save(post);

        Comment comment1 = createComment("already i had lunch",member, post);
        Comment comment2 = createComment("i dont have lunch",member, post);

        commentRepository.save(comment1);
        commentRepository.save(comment2);

        Session session = member.addSession();
        Cookie cookie = new Cookie("SESSION",session.getAccessToken());

        //when
        mockMvc.perform(get(COMMON_URL+"/{postId}",postId)
                        .cookie(cookie))
                .andExpect(status().isOk())
                .andDo(print());

        Post findByPost = postRepository.findByOne(postId)
                .orElseThrow(() -> new PostNotFound());
        //then
        assertThat(findByPost.getComments().size()).isEqualTo(2);
    }

    private Member createMember(String email,String password,String nickname,String intro,String phoneNumber,String birthDay) {
        Member member = Member.builder()
                .memberEmail(email)
                .password(password)
                .nickname(nickname)
                .intro(intro)
                .phoneNumber(phoneNumber)
                .birthDay(birthDay)
                .build();
        return member;
    }

    private Post createPost(Member member,String content) {
        Post post = Post.builder()
                .content(content)
                .deleteYn("N")
                .likeNumber(0L)
                .member(member)
                .build();
        return post;
    }

    private Comment createComment(String content,Member member, Post post) {
        Comment comment1 = Comment.builder()
                .content(content)
                .likeNumber(0L)
                .deleteYn("N")
                .post(post)
                .member(member)
                .build();
        return comment1;
    }
}