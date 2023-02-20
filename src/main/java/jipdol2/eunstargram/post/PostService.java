package jipdol2.eunstargram.post;

import jipdol2.eunstargram.image.ImageService;
import jipdol2.eunstargram.image.dto.ImageDTO;
import jipdol2.eunstargram.image.entity.Image;
import jipdol2.eunstargram.image.entity.ImageCode;
import jipdol2.eunstargram.image.entity.ImageJpaRepository;
import jipdol2.eunstargram.member.entity.Member;
import jipdol2.eunstargram.member.entity.MemberJpaRepository;
import jipdol2.eunstargram.member.entity.MemberRepository;
import jipdol2.eunstargram.post.dto.request.PostSaveRequestDTO;
import jipdol2.eunstargram.post.dto.response.PostResponseDTO;
import jipdol2.eunstargram.post.entity.Post;
import jipdol2.eunstargram.post.entity.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {

    //Service
    private final ImageService imageService;
    //Repository
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final MemberJpaRepository memberJpaRepository;
    private final ImageJpaRepository imageJpaRepository;

    @Transactional
    public Long save(PostSaveRequestDTO postDto) {
        /**
         * 2023/01/10
         * TODO Post Entity 에는 member 객체가 존재, insert 해주어야함
         * TODO => 연관관계 편의 메소드 사용
         */
        Member member = memberRepository.findByOne(postDto.getMemberId())
                .orElseThrow(()->new IllegalArgumentException("memberId가 존재하지 않습니다."));

        Image image = uploadPostImage(postDto.getImage());

        return postRepository.save(Post.builder()
                .likeNumber(0L)
                .content(postDto.getContent())
                .member(member)
                .image(image)
                .build());
    }

    @Transactional
    public List<PostResponseDTO> findByAll(Long memberId){

        Member findByMember = memberJpaRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("회원정보가 존재하지 않습니다."));

        List<Post> findByPosts = postRepository.findMemberIdByAll(findByMember.getId());

        return findByPosts.stream()
                .map(PostService::apply)
                .collect(Collectors.toList());
    }

    private static PostResponseDTO apply(Post p) {
        return new PostResponseDTO(p, new ImageDTO(p.getImage()));
    }

    @Transactional
    public Image uploadPostImage(MultipartFile imageDTO){

        String imageName = imageService.uploadImage(imageDTO);

        //TODO: 후에 memberId 를 session or token 에서 가져온 값으로 변경 필요
        Member findByMember = memberJpaRepository.findById(1L)
                .orElseThrow(() -> new IllegalArgumentException("회원정보가 존재하지 않습니다."));

        Image image = Image.builder()
                .originalFileName(imageDTO.getOriginalFilename())
                .storedFileName(imageName)
                .member(findByMember)
                .imageCode(ImageCode.POST)
                .build();

        imageJpaRepository.save(image);

        return image;
    }


    public void edit(){

    }
}