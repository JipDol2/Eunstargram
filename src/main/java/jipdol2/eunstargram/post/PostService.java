package jipdol2.eunstargram.post;

import jipdol2.eunstargram.common.dto.EmptyJSON;
import jipdol2.eunstargram.config.data.UserSession;
import jipdol2.eunstargram.exception.MemberNotFound;
import jipdol2.eunstargram.exception.MissAuthorized;
import jipdol2.eunstargram.exception.PostNotFound;
import jipdol2.eunstargram.image.ImageService;
import jipdol2.eunstargram.image.dto.response.ImageResponseDTO;
import jipdol2.eunstargram.image.entity.Image;
import jipdol2.eunstargram.image.entity.ImageCode;
import jipdol2.eunstargram.image.entity.ImageJpaRepository;
import jipdol2.eunstargram.member.entity.Member;
import jipdol2.eunstargram.member.entity.MemberJpaRepository;
import jipdol2.eunstargram.member.entity.MemberRepository;
import jipdol2.eunstargram.post.dto.request.PostEditRequestDTO;
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
@Transactional
public class PostService {

    //Service
    private final ImageService imageService;
    //Repository
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final MemberJpaRepository memberJpaRepository;
    private final ImageJpaRepository imageJpaRepository;

    public Long save(Long memberId,PostSaveRequestDTO postDto) {

        Member member = memberRepository.findByOne(memberId)
                .orElseThrow(()->new MemberNotFound());

        Image image = uploadPostImage(memberId,postDto.getImage());

        return postRepository.save(Post.builder()
                .likeNumber(0L)
                .content(postDto.getContent())
                .deleteYn("N")
                .member(member)
                .image(image)
                .build());
    }

    @Transactional(readOnly = true)
    public List<PostResponseDTO> findByAll(String nickname){

        Member findByMember = memberJpaRepository.findByNickname(nickname)
                .orElseThrow(() -> new MemberNotFound());

        //fetch join 으로 post 와 image 조회 쿼리 개선
        List<Post> findByPosts = postRepository.findByAll(findByMember.getId());

        return findByPosts.stream()
                .filter(p-> "N".equals(p.getDeleteYn()))
                .map(PostService::apply)
                .collect(Collectors.toList());
    }

    public PostResponseDTO findByOne(Long postId){

        Post post = postRepository.findByOne(postId)
                .orElseThrow(() -> new PostNotFound());

        return apply(post);
    }

    private static PostResponseDTO apply(Post p) {
        return new PostResponseDTO(p, new ImageResponseDTO(p.getImage()));
    }

    public Image uploadPostImage(Long id,MultipartFile imageDTO){

        String imageName = imageService.uploadImage(imageDTO);

        Member findByMember = memberJpaRepository.findById(id)
                .orElseThrow(() -> new MemberNotFound());

        Image image = Image.builder()
                .originalFileName(imageDTO.getOriginalFilename())
                .storedFileName(imageName)
                .member(findByMember)
                .imageCode(ImageCode.POST)
                .build();

        imageJpaRepository.save(image);

        return image;
    }

    public EmptyJSON edit(Long postId,PostEditRequestDTO postEditDto){

        Post findByPost = postRepository.findByOne(postId)
                .orElseThrow(() -> new PostNotFound());

        findByPost.edit(postEditDto);

        postRepository.save(findByPost);

        return new EmptyJSON();
    }

    public EmptyJSON deletePost(Long memberId,Long postId){

        Member member = memberRepository.findByOne(memberId)
                .orElseThrow(() -> new MemberNotFound());

        Post findByPost = postRepository.findByOne(postId)
                .orElseThrow(() -> new PostNotFound());

        Member findByMember = findByPost.getMember();

        if(member!=findByMember){
            throw new MissAuthorized();
        }

        findByPost.changeDeleteYn("Y");
        return new EmptyJSON();
    }
}