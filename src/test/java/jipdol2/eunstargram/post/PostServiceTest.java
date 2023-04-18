package jipdol2.eunstargram.post;

import jipdol2.eunstargram.image.entity.Image;
import jipdol2.eunstargram.image.entity.ImageCode;
import jipdol2.eunstargram.image.entity.ImageJpaRepository;
import jipdol2.eunstargram.jwt.JwtManager;
import jipdol2.eunstargram.jwt.dto.UserSessionDTO;
import jipdol2.eunstargram.member.entity.Member;
import jipdol2.eunstargram.member.entity.MemberRepository;
import jipdol2.eunstargram.post.dto.request.PostSaveRequestDTO;
import jipdol2.eunstargram.post.dto.response.PostResponseDTO;
import jipdol2.eunstargram.post.entity.Post;
import jipdol2.eunstargram.post.entity.PostRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.Cookie;
import java.io.FileInputStream;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class PostServiceTest {

    @Autowired
    private PostService postService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private ImageJpaRepository imageJpaRepository;

    @Autowired
    private JwtManager jwtManager;

    @Test
    @DisplayName("게시글 저장 테스트")
    void savePostTest() throws Exception {
        //given
        Member member = createMember();
        PostSaveRequestDTO postSaveRequestDTO = createPostRequestDTO();
        memberRepository.save(member);

        //when
        Long postId = postService.save(member.getId(), postSaveRequestDTO);

        //then
        PostResponseDTO findByPost = postService.findByOne(postId);
        assertThat(postId).isEqualTo(findByPost.getId());
    }

    @Test
    @DisplayName("모든 게시글 조회 테스트")
    void findAllPostTest() throws Exception {
        //given
        Member member = createMember();
        Image image = createImage(member);
        Post[] post = new Post[2];
        IntStream.range(0, 2).forEach(i -> post[i] = createPost(i + "번째 게시글", member, image));

        //when
        memberRepository.save(member);
        imageJpaRepository.save(image);
        Arrays.stream(post).forEach(p -> postRepository.save(p));

        //then
        List<PostResponseDTO> findAllByPosts = postService.findByAll("Rabbit96");
        for(int i=0;i<2;i++){
            assertThat(findAllByPosts.get(i).getContent()).isEqualTo(post[i].getContent());
        }
    }

    @Test
    @DisplayName("게시글 삭제 테스트")
    void deletePostTest() throws Exception{
        //given
        Member member = createMember();
        Image image = createImage(member);
        Post post = createPost("1 번째 게시글",member,image);

        //when
        memberRepository.save(member);
        Long postId = postRepository.save(post);

        UserSessionDTO sessionDTO = UserSessionDTO.builder()
                .id(member.getId())
                .email(member.getMemberEmail())
                .nickname(member.getNickname())
                .build();

        //then
        postService.deletePost(sessionDTO.getId(),postId);
        Post findByPost = postRepository.findByOne(postId)
                .get();
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
                .member(member)
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
