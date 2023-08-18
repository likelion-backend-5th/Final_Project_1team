/**
 * @project backend
 * @author ARA
 * @since 2023-08-18 AM 9:19
 */

package mutsa.api.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mutsa.api.dto.article.ArticleCreateRequestDto;
import mutsa.api.dto.article.ArticleResponseDto;
import mutsa.api.dto.article.ArticleOrderDirection;
import mutsa.api.dto.article.ArticleUpdateRequestDto;
import mutsa.api.service.article.ArticleService;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/api/articles")
@RequiredArgsConstructor
public class ArticleController {
    private final ArticleService articleService;

    //  조회
    @GetMapping
    public ResponseEntity<List<ArticleResponseDto>> getArticleList(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "20") Integer size,
            @RequestParam(value = "order", defaultValue = "DESC") ArticleOrderDirection direction
            ) {
        return ResponseEntity.ok(articleService.getPage(page, size, Sort.Direction.fromString(direction.name())));
    }

    @GetMapping("/{articleApiId}")
    public ResponseEntity<ArticleResponseDto> getArticle(
            @PathVariable(value = "articleApiId") String articleApiId
    ) {
        return ResponseEntity.ok(articleService.read(articleApiId));
    }

    //  등록
    @PostMapping
    public ResponseEntity<ArticleResponseDto> createArticle(@Valid @RequestBody ArticleCreateRequestDto createRequestDto) {
        return ResponseEntity.ok(articleService.save(createRequestDto));
    }

    //  수정
    @PutMapping
    public ResponseEntity<ArticleResponseDto> updateArticle(@Valid @RequestBody ArticleUpdateRequestDto updateRequestDto) {
        return ResponseEntity.ok(articleService.update(updateRequestDto));
    }

    //  삭제
    @DeleteMapping("/{articleApiId}")
    public ResponseEntity<?> deleteArticle(
            @PathVariable("articleApiId") String apiId
    ) {
        articleService.delete(apiId);
        return ResponseEntity.ok().body(null);
    }

    //  DEBUG 디버깅용 테스트 메소드
    @PostMapping("/dummy-article")
    public ResponseEntity<List<ArticleResponseDto>> createDummyArticle(
            @RequestParam(value = "amount", defaultValue = "1") Integer count
    ) {
        return ResponseEntity.ok(articleService.saveDummyArticles(count));
    }
}
