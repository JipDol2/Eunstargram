package jipdol2.eunstargram.post;

import com.fasterxml.jackson.databind.ObjectMapper;
import jipdol2.eunstargram.member.entity.Member;
import jipdol2.eunstargram.member.entity.MemberRepository;
import jipdol2.eunstargram.post.dto.request.PostSaveRequestDTO;
import jipdol2.eunstargram.post.entity.PostRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PostService postService;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String COMMON_URL = "/api/post";

    @Test
    @DisplayName("게시글 업로드시 200 status code 리턴")
    @Transactional
    void uploadPostTest() throws Exception {

        //given
        Member member = createMember();
        memberRepository.save(member);

        PostSaveRequestDTO postSaveRequestDTO = createPostRequestDTO();

        String json = objectMapper.writeValueAsString(postSaveRequestDTO);

        //when
        mockMvc.perform(MockMvcRequestBuilders.post(COMMON_URL+ "/upload")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
        )
                .andExpect(status().isOk())
                .andDo(print());

        //then
        assertThat(postRepository.findByAll(1l).size()).isEqualTo(1);
    }

    @Test
    @DisplayName("게시글 전체 조회 200 status code 리턴 + 게시글 리턴")
    @Transactional
    void findByAllPosts() throws Exception{

        //given
        Member member = createMember();
        memberRepository.save(member);

        PostSaveRequestDTO postSaveRequestDTO = createPostRequestDTO();

//        String json = objectMapper.writeValueAsString(postSaveRequestDTO);
        postService.save(postSaveRequestDTO);
        //when
        mockMvc.perform(MockMvcRequestBuilders.get(COMMON_URL+"/{id}","1"))
                .andExpect(jsonPath("$[0].imagePath").value("D:/upload"))
                .andExpect(jsonPath("$[0].likeNumber").value(2L))
                .andExpect(jsonPath("$[0].content").value("행복한 하루"))
                .andExpect(jsonPath("$[0].memberId").value(1L))
                .andDo(print());
        //then
        assertThat(postRepository.findByAll(1l).size()).isEqualTo(1);
    }

    private Member createMember() {
        Member member = Member.builder()
                .memberId("testId")
                .password("1234")
                .nickname("Rabbit96")
                .birthDay("19940715")
                .intro("life is one time")
                .imagePath("D:/save")
                .deleteYn("N")
                .build();
        return member;
    }

    private PostSaveRequestDTO createPostRequestDTO() {
        PostSaveRequestDTO postSaveRequestDTO = PostSaveRequestDTO.builder()
                .imagePath("D:/upload")
                .likeNumber(2L)
                .content("행복한 하루")
                .memberId(1L)
                .build();
        return postSaveRequestDTO;
    }
}