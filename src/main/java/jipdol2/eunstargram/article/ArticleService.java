package jipdol2.eunstargram.article;

import jipdol2.eunstargram.article.dto.ArticleDTO;
import jipdol2.eunstargram.article.dto.request.ArticleSaveRequestDTO;
import jipdol2.eunstargram.article.entity.Article;
import jipdol2.eunstargram.article.entity.ArticleRepository;
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
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public EmptyJSON save(ArticleSaveRequestDTO articleDTO) {
        /**
         * TODO 2023/01/10 Article Entity 에는 member 객체가 존재, insert 해주어야함
         * TODO => 연관관계 편의 메소드 사용
         */
        Member member = memberRepository.findByOne(articleDTO.getMemberId())
                .orElseThrow(()->new IllegalArgumentException("memberId가 존재하지 않습니다."));

        articleRepository.save(Article.builder()
                .imagePath(articleDTO.getImagePath())
                .likeNumber(articleDTO.getLikeNumber())
                .content(articleDTO.getContent())
                .member(member)
                .build());
        return new EmptyJSON();
    }

    @Transactional
    public List<ArticleDTO> findAll(Long memberId){
        return articleRepository.findByAll(memberId);
    }
}