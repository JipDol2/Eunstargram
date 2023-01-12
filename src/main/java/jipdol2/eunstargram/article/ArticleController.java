package jipdol2.eunstargram.article;

import jipdol2.eunstargram.article.dto.ArticleDTO;
import jipdol2.eunstargram.article.dto.request.ArticleSaveRequestDTO;
import jipdol2.eunstargram.common.EmptyJSON;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/api/article")
public class ArticleController {

    private final ArticleService articleService;

    //TODO: 2023/01/10 게시글 업로드 API 생성
    @PostMapping("/upload")
    public ResponseEntity<EmptyJSON> uploadArticle(@RequestBody ArticleSaveRequestDTO articleDTO){

        log.info("articleDTO={}",articleDTO.toString());
        return ResponseEntity.status(HttpStatus.OK).body(articleService.save(articleDTO));
    }

    //TODO: 2023/01/12 전체 게시글 조회
    @GetMapping("/all-articles/{id}")
    public ResponseEntity<List<ArticleDTO>> findAllArticles(@PathVariable("id") Long id){
        log.info("memberId={}",id);
        return ResponseEntity.status(HttpStatus.OK).body(articleService.findAll(id));
    }
}
