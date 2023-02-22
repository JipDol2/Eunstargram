package jipdol2.eunstargram.post;

import com.fasterxml.jackson.databind.ObjectMapper;
import jipdol2.eunstargram.image.entity.Image;
import jipdol2.eunstargram.image.entity.ImageCode;
import jipdol2.eunstargram.member.entity.Member;
import jipdol2.eunstargram.member.entity.MemberRepository;
import jipdol2.eunstargram.post.dto.request.PostEditRequestDTO;
import jipdol2.eunstargram.post.dto.request.PostSaveRequestDTO;
import jipdol2.eunstargram.post.entity.Post;
import jipdol2.eunstargram.post.entity.PostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.FileInputStream;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
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

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String COMMON_URL = "/api/post";

    @BeforeEach
    void clean(){
        this.entityManager
                .createNativeQuery("ALTER TABLE MEMBER AUTO_INCREMENT = 1")
                .executeUpdate();
    }

    @Test
    @DisplayName("게시글 업로드 : 게시글 업로드시 200 status code 리턴")
    @Transactional
    void uploadPostTest() throws Exception {

        //given
        Member member = createMember();
        memberRepository.save(member);

        PostSaveRequestDTO postSaveRequestDTO = createPostRequestDTO();

        //when
        mockMvc.perform(MockMvcRequestBuilders.multipart(COMMON_URL+ "/upload")
                        .file((MockMultipartFile)postSaveRequestDTO.getImage())
                        .param("likeNumber",Long.toString(postSaveRequestDTO.getLikeNumber()))
                        .param("content",postSaveRequestDTO.getContent())
                        .param("memberId",Long.toString(postSaveRequestDTO.getMemberId()))
                )
                .andExpect(status().isOk())
                .andDo(print());

        //then
        assertThat(postRepository.findByAll(1l).size()).isEqualTo(1);
    }

    @Test
    @DisplayName("게시글 전체 조회 : 게시글 전체 조회 200 status code 리턴 + 게시글 리턴")
    @Transactional
    void findByAllPosts() throws Exception{

        //given
        Member member = createMember();
        memberRepository.save(member);

        List<PostSaveRequestDTO> postRequestListDTO = createPostRequestListDTO();
        postRequestListDTO.stream().forEach(postService::save);

//        String json = objectMapper.writeValueAsString(postSaveRequestDTO);


        //when
        mockMvc.perform(MockMvcRequestBuilders.get(COMMON_URL+"/{id}","1"))
                .andExpect(jsonPath("$[0].likeNumber").value(0L))
                .andExpect(jsonPath("$[0].content").value("행복한 하루"))
                .andExpect(jsonPath("$[0].memberId").value(1L))
                .andExpect(jsonPath("$[0].imageDTO.originalFileName").value("testImage.jpg"))
                .andExpect(jsonPath("$[1].likeNumber").value(0L))
                .andExpect(jsonPath("$[1].content").value("웃는 하루"))
                .andExpect(jsonPath("$[1].memberId").value(1L))
                .andExpect(jsonPath("$[1].imageDTO.originalFileName").value("testImage.jpg"))
                .andDo(print());
        //then
        assertThat(postRepository.findByAll(1l).size()).isEqualTo(2);
    }

    @Test
    @DisplayName("게시글 수정 : 게시글 수정시 200 status code 리턴")
    @Transactional
    void editPost() throws Exception{

        //given
        Member member = createMember();
        memberRepository.save(member);

        Post post = Post.builder()
                .content("나의 삶의 찬란한 시간만 비추길")
                .member(member)
                .image(createImage(member))
                .build();
        postRepository.save(post);

        PostEditRequestDTO postEditRequestDTO = PostEditRequestDTO.builder()
                .content("너는 나의 봄이었다")
                .build();

        String json = objectMapper.writeValueAsString(postEditRequestDTO);

        //then
        mockMvc.perform(MockMvcRequestBuilders.put(COMMON_URL+"/{memberId}/{postId}",member.getId(),post.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
        )
                .andExpect(status().isOk())
                .andDo(print());

        //when
        Post findByPost = postRepository.findByOne(post.getId())
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));

        assertThat(findByPost.getContent()).isEqualTo("너는 나의 봄이었다");
    }

    private Member createMember() {
        Member member = Member.builder()
                .memberId("testId")
                .password("1234")
                .nickname("Rabbit96")
                .birthDay("19940715")
                .intro("life is one time")
                .deleteYn("N")
                .build();
        return member;
    }

    private Image createImage(Member member){

        String originalFileName = "testImage.jpg";

        String uuid = UUID.randomUUID().toString();
        String imageName = uuid + "_" + originalFileName;

        Image image = Image.builder()
                .originalFileName(originalFileName)
                .storedFileName(imageName)
                .member(member)
                .imageCode(ImageCode.POST)
                .build();
        return image;
    }

    private PostSaveRequestDTO createPostRequestDTO() throws Exception{
        PostSaveRequestDTO postSaveRequestDTO = PostSaveRequestDTO.builder()
                .content("행복한 하루")
                .memberId(1L)
                .image(createMultipartFile())
                .build();
        return postSaveRequestDTO;
    }

    private List<PostSaveRequestDTO> createPostRequestListDTO() throws Exception{
        return List.of(
                PostSaveRequestDTO.builder()
                        .content("행복한 하루")
                        .memberId(1L)
                        .image(createMultipartFile())
                        .build(),
                PostSaveRequestDTO.builder()
                        .content("웃는 하루")
                        .memberId(1L)
                        .image(createMultipartFile())
                        .build()
        );
    }

    private MockMultipartFile createMultipartFile() throws Exception {
        String originalFileName = "testImage.jpg";
        String path = "src/test/resources/img/" + originalFileName;

        return new MockMultipartFile("image", originalFileName, "image/jpg",new FileInputStream(path));
    }

}