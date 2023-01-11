package jipdol2.eunstargram.article;

import jipdol2.eunstargram.article.dto.ArticleDTO;
import jipdol2.eunstargram.article.entity.Article;
import jipdol2.eunstargram.article.entity.ArticleRepository;
import jipdol2.eunstargram.common.EmptyJSON;
import jipdol2.eunstargram.member.entity.Member;
import jipdol2.eunstargram.member.entity.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public EmptyJSON save(ArticleDTO articleDTO) {
        /**
         * TODO 2023/01/10 Article Entity 에는 member 객체가 존재, insert 해주어야함
         * TODO 2023/01/10 따로 공부 필요...
         */
        Member member = memberRepository.findByOneId("testId")
                        .get(0);

        articleRepository.save(Article.builder()
                .imagePath(articleDTO.getImagePath())
                .likeNumber(articleDTO.getLikeNumber())
                .content(articleDTO.getContent())
                .member(member)
                .build());
        return new EmptyJSON();
    }
}