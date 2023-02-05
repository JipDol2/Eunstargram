package jipdol2.eunstargram.post;

import jipdol2.eunstargram.image.ImageService;
import jipdol2.eunstargram.image.entity.Image;
import jipdol2.eunstargram.image.entity.ImageCode;
import jipdol2.eunstargram.image.entity.ImageJpaRepository;
import jipdol2.eunstargram.member.entity.MemberJpaRepository;
import jipdol2.eunstargram.post.dto.response.PostResponseDTO;
import jipdol2.eunstargram.post.dto.request.PostSaveRequestDTO;
import jipdol2.eunstargram.post.entity.Post;
import jipdol2.eunstargram.post.entity.PostRepository;
import jipdol2.eunstargram.common.dto.EmptyJSON;
import jipdol2.eunstargram.member.entity.Member;
import jipdol2.eunstargram.member.entity.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.TransactionScoped;
import java.util.List;

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
    public EmptyJSON save(PostSaveRequestDTO postDto) {
        /**
         * 2023/01/10
         * TODO Post Entity 에는 member 객체가 존재, insert 해주어야함
         * TODO => 연관관계 편의 메소드 사용
         */
        Member member = memberRepository.findByOne(postDto.getMemberId())
                .orElseThrow(()->new IllegalArgumentException("memberId가 존재하지 않습니다."));

        Image image = uploadPostImage(postDto.getImage());

        postRepository.save(Post.builder()
                .likeNumber(postDto.getLikeNumber())
                .content(postDto.getContent())
                .member(member)
                .image(image)
                .build());

        /**
         * 2023/02/05
         * TODO 게시글을 업로드
         * TODO 업로드한 게시글 목록(전체)들을 조회하여 클라이언트에게 전달
         */
        return new EmptyJSON();
    }

    @Transactional
    public List<PostResponseDTO> findByAll(Long memberSeq){
        return postRepository.findByAll(memberSeq);
    }

    @Transactional
    public List<PostResponseDTO> findByAll(String memberId){
        return postRepository.findByAll(memberId);
    }

    @Transactional
    public Image uploadPostImage(MultipartFile imageDTO){

        String imageName = imageService.uploadImage(imageDTO);

        //TODO: 후에 memberId 를 session 에서 가져온 값으로 변경 필요
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
}