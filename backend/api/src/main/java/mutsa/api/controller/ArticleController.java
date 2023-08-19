/**
 * @project backend
 * @author ARA
 * @since 2023-08-18 AM 9:19
 */

package mutsa.api.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mutsa.api.dto.article.*;
import mutsa.api.service.article.ArticleService;
import mutsa.common.domain.models.Status;
import mutsa.common.domain.models.article.ArticleStatus;
import org.springframework.data.domain.Page;
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
    public ResponseEntity<Page<ArticleResponseDto>> getArticleList(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "20") Integer size,
            @RequestParam(value = "order", defaultValue = "DESC") ArticleOrderDirection direction,
            @RequestParam(value = "articleStatue", defaultValue = "LIVE") ArticleStatus articleState,
            @RequestParam(value = "statue", defaultValue = "ACTIVE") Status status,
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "username", required = false) String username
    ) {
        return ResponseEntity.ok(articleService.getPage(
                page,
                size,
                Sort.Direction.fromString(direction.name()),
                ArticleFilterDto.of(status, articleState, title, description, username)
        ));
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

    //  DEBUG 디버깅용 테스트 메소드
    @DeleteMapping("/{articleApiId}/test")
    public ResponseEntity<?> deleteArticleTest(
            @PathVariable("articleApiId") String apiId
    ) {
        articleService.deleteByApiId(apiId);
        return ResponseEntity.ok().body(null);
    }
}
