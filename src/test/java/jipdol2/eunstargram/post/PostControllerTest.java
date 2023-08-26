package jipdol2.eunstargram.post;

import com.fasterxml.jackson.databind.ObjectMapper;
import jipdol2.eunstargram.exception.post.PostNotFound;
import jipdol2.eunstargram.image.entity.Image;
import jipdol2.eunstargram.image.entity.ImageCode;
import jipdol2.eunstargram.image.entity.ImageJpaRepository;
import jipdol2.eunstargram.jwt.JwtManager;
import jipdol2.eunstargram.jwt.dto.UserSessionDTO;
import jipdol2.eunstargram.member.entity.Member;
import jipdol2.eunstargram.member.entity.MemberRepository;
import jipdol2.eunstargram.post.dto.request.PostEditRequestDTO;
import jipdol2.eunstargram.post.dto.request.PostSaveRequestDTO;
import jipdol2.eunstargram.post.entity.Post;
import jipdol2.eunstargram.post.entity.PostRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.servlet.http.Cookie;
import java.io.FileInputStream;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
@Transactional
class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PostService postService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private ImageJpaRepository imageJpaRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JwtManager jwtManager;

    private static final String COMMON_URL = "/api/post";

//    @BeforeEach
//    void clean(){
//        this.entityManager
//                .createNativeQuery("ALTER TABLE MEMBER AUTO_INCREMENT = 1")
//                .executeUpdate();
//    }

    @Test
    @DisplayName("게시글 업로드 : 게시글 업로드시 200 status code 리턴")
    void uploadPostTest() throws Exception {

        //given
        Member member = createMember();
        memberRepository.save(member);

//        Session session = member.addSession();
        UserSessionDTO sessionDTO = UserSessionDTO.builder()
                .id(member.getId())
                .email(member.getMemberEmail())
                .nickname(member.getNickname())
                .build();
        String accessToken = jwtManager.makeAccessToken(sessionDTO.getId());
        Cookie cookie = new Cookie("SESSION",accessToken);

        PostSaveRequestDTO postSaveRequestDTO = createPostRequestDTO();

        //when
        mockMvc.perform(multipart(COMMON_URL+ "/upload")
                        .file((MockMultipartFile)postSaveRequestDTO.getImage())
                        .param("content",postSaveRequestDTO.getContent())
//                        .param("memberId",Long.toString(postSaveRequestDTO.getMemberId()))
                        .cookie(cookie))
                .andExpect(status().isOk())
                .andDo(print());

        //then
        assertThat(postRepository.findByAll(member.getId()).size()).isEqualTo(1);
    }

    @Test
    @DisplayName("게시글 업로드 : 게시글 업로드시 Image 파일은 필수입니다")
    void uploadPostImageTest() throws Exception {

        //given
        Member member = createMember();
        memberRepository.save(member);

//        Session session = member.addSession();
        UserSessionDTO sessionDTO = UserSessionDTO.builder()
                .id(member.getId())
                .email(member.getMemberEmail())
                .nickname(member.getNickname())
                .build();
        String accessToken = jwtManager.makeAccessToken(sessionDTO.getId());
        Cookie cookie = new Cookie("SESSION",accessToken);

        PostSaveRequestDTO postSaveRequestDTO = createPostRequestDTO();
        postSaveRequestDTO.setImage(null);

        //when
        mockMvc.perform(multipart(COMMON_URL+ "/upload")
                        .file("image",null)
                        .param("content",postSaveRequestDTO.getContent())
                        .cookie(cookie))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.message").value("Bad Request"))
                .andExpect(jsonPath("$.validation.image").value("이미지 파일은 필수입니다."))
                .andDo(print());
    }

    @Test
    @DisplayName("게시글 전체 조회 : 게시글 전체 조회 200 status code 리턴 + 게시글 리턴")
    void findByAllPosts() throws Exception{

        //given
        Member member = createMember();
        memberRepository.save(member);

        Image image = createImage(member);
        imageJpaRepository.save(image);

        List<Post> postRequestListDTO = createPostRequestListDTO(member,image);
        postRequestListDTO.stream().forEach(postRepository::save);

        //when
        mockMvc.perform(get(COMMON_URL+"/{nickname}",member.getNickname()))
                .andExpect(jsonPath("$.data[0].likeNumber").value(0L))
                .andExpect(jsonPath("$.data[0].content").value("Im kim da mi!!"))
                .andExpect(jsonPath("$.data[0].memberId").value(member.getId()))
                .andExpect(jsonPath("$.data[0].imageResponseDTO.originalFileName").value("testImage.jpg"))
                .andExpect(jsonPath("$.data[1].likeNumber").value(0L))
                .andExpect(jsonPath("$.data[1].content").value("Im not puppy!!"))
                .andExpect(jsonPath("$.data[1].memberId").value(member.getId()))
                .andExpect(jsonPath("$.data[1].imageResponseDTO.originalFileName").value("testImage.jpg"))
                .andDo(print());

        //then
        assertThat(postRepository.findByAll(member.getId()).size()).isEqualTo(2);
    }

    @Test
    @DisplayName("게시글 수정 : 게시글 수정시 200 status code 리턴")
    void editPost() throws Exception{

        //given
        Member member = createMember();
        memberRepository.save(member);

        Image image = createImage(member);
        imageJpaRepository.save(image);

        Post post = createPost("나의 삶의 찬란한 시간만 비추길", member,image);
//        Post post = createPost("나의 삶의 찬란한 시간만 비추길", member);
        postRepository.save(post);

        PostEditRequestDTO postEditRequestDTO = PostEditRequestDTO.builder()
                .content("너는 나의 봄이었다")
                .build();

//        Session session = member.addSession();
//        Cookie cookie = new Cookie("SESSION",session.getAccessToken());

        String json = objectMapper.writeValueAsString(postEditRequestDTO);

        //when
        mockMvc.perform(put(COMMON_URL+"/p/{postId}",post.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andDo(print());

        //then
        Post findByPost = postRepository.findByOne(post.getId())
                .orElseThrow(() -> new PostNotFound());

        assertThat(findByPost.getContent()).isEqualTo("너는 나의 봄이었다");
    }

    @Test
    @DisplayName("게시글 삭제 : 게시글 삭제시 200 status code 리턴")
    void deletePost() throws Exception {

        //given
        Member member = createMember();
        memberRepository.save(member);

        /**
         * CascadeType.ALL
         * - 밑에 두줄을 정의하지 않고 테스트를 진행했더니 에러(object references an unsaved transient instance)가 터짐
         *         Image image = createImage(member);
         *         imageJpaRepository.save(image);
         * - 원인 :
         *          1. post 와 image 관계에서 cascade 설정을 해주지 않은 상태에서 image 를 persist 해주지 않았기 때문
         * - 해결 :
         *          1. post 와 image(N:1) 관계에 cascade option 을 정의해주면 된다.
         *          2. image 를 먼저 persist 해준 후 persist 된 image를 post에도 파라미터로 정의하면 된다.
         */

        Image image = createImage(member);
        imageJpaRepository.save(image);

        Post post = createPost("나의 삶의 찬란한 시간만 비추길", member,image);
        Long postId = postRepository.save(post);

//        Session session = member.addSession();
        UserSessionDTO sessionDTO = UserSessionDTO.builder()
                .id(member.getId())
                .email(member.getMemberEmail())
                .nickname(member.getNickname())
                .build();
        String accessToken = jwtManager.makeAccessToken(sessionDTO.getId());
        Cookie cookie = new Cookie("SESSION",accessToken);

        //when
        mockMvc.perform(delete(COMMON_URL+"/p/delete/{postId}",postId)
                        .cookie(cookie))
                .andExpect(status().isOk())
                .andDo(print());

        //then
        Post findByPost = postRepository.findByOne(post.getId())
                .orElseThrow(() -> new PostNotFound());

        assertThat(findByPost.getDeleteYn()).isEqualTo("Y");
    }

    private Member createMember() {
        Member member = Member.builder()
                .memberEmail("jipdol2@gmail.com")
                .password("1234")
                .nickname("Rabbit96")
                .birthDay("19940715")
                .intro("life is one time")
                .build();
        return member;
    }

    private Post createPost(String content, Member member, Image image) {
        return Post.builder()
                .likeNumber(0L)
                .content(content)
                .deleteYn("N")
                .member(member)
                .image(image)
                .build();
    }

    private Image createImage(Member member){

        String originalFileName = "testImage.jpg";

        String uuid = UUID.randomUUID().toString();
        String imageName = uuid + "_" + originalFileName;

        Image image = Image.builder()
                .originalFileName(originalFileName)
                .storedFileName(imageName)
                .imageCode(ImageCode.POST)
                .build();
        return image;
    }

    private PostSaveRequestDTO createPostRequestDTO() throws Exception{
        PostSaveRequestDTO postSaveRequestDTO = PostSaveRequestDTO.builder()
                .content("행복한 하루")
                .image(createMultipartFile())
                .build();
        return postSaveRequestDTO;
    }

    private List<Post> createPostRequestListDTO(Member member, Image image) throws Exception{
        return List.of(
                Post.builder()
                    .likeNumber(0L)
                    .content("Im kim da mi!!")
                    .deleteYn("N")
                    .member(member)
                    .image(image)
                    .build(),
                Post.builder()
                    .likeNumber(0L)
                    .content("Im not puppy!!")
                    .deleteYn("N")
                    .member(member)
                    .image(image)
                    .build()
        );
    }

    private MockMultipartFile createMultipartFile() throws Exception {
        String originalFileName = "testImage.jpg";
        String path = "src/test/resources/img/" + originalFileName;

        return new MockMultipartFile("image", originalFileName, "image/jpg",new FileInputStream(path));
    }

}