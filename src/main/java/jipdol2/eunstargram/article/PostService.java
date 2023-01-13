package jipdol2.eunstargram.article;

import jipdol2.eunstargram.article.dto.PostDTO;
import jipdol2.eunstargram.article.dto.request.PostSaveRequestDTO;
import jipdol2.eunstargram.article.entity.Post;
import jipdol2.eunstargram.article.entity.PostRepository;
import jipdol2.eunstargram.common.EmptyJSON;
import jipdol2.eunstargram.member.entity.Member;
import jipdol2.eunstargram.member.entity.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public EmptyJSON save(PostSaveRequestDTO postDto) {
        /**
         * TODO 2023/01/10 Post Entity 에는 member 객체가 존재, insert 해주어야함
         * TODO => 연관관계 편의 메소드 사용
         */
        Member member = memberRepository.findByOne(postDto.getMemberId())
                .orElseThrow(()->new IllegalArgumentException("memberId가 존재하지 않습니다."));

        postRepository.save(Post.builder()
                .imagePath(postDto.getImagePath())
                .likeNumber(postDto.getLikeNumber())
                .content(postDto.getContent())
                .member(member)
                .build());
        return new EmptyJSON();
    }

    @Transactional
    public List<PostDTO> findAll(Long memberId){
        return postRepository.findByAll(memberId);
    }
}