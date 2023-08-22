/**
 * @project backend
 * @author ARA
 * @since 2023-08-16 PM 1:18
 */

package mutsa.api.service.article;

import lombok.RequiredArgsConstructor;
import mutsa.api.dto.article.ArticleCreateRequestDto;
import mutsa.api.dto.article.ArticleFilterDto;
import mutsa.api.dto.article.ArticleResponseDto;
import mutsa.api.dto.article.ArticleUpdateRequestDto;
import mutsa.api.util.SecurityUtil;
import mutsa.common.domain.filter.article.ArticleFilter;
import mutsa.common.domain.models.article.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ArticleService {
    private final ArticleModuleService articleModuleService;

    public ArticleResponseDto save(ArticleCreateRequestDto requestDto) {
        return ArticleResponseDto.to(articleModuleService.save(requestDto));
    }

    public ArticleResponseDto update(ArticleUpdateRequestDto updateDto) {
        return ArticleResponseDto.to(articleModuleService.update(updateDto));
    }

    protected Article getByApiId(String apiId) {
        return articleModuleService.getByApiId(apiId);
    }

    protected Article getById(Long id) {
        return articleModuleService.getById(id);
    }

    public ArticleResponseDto read(String apiId) {
        return ArticleResponseDto.to(articleModuleService.getByApiId(apiId));
    }

    public Page<ArticleResponseDto> getPageByUsername(
            String username, Sort.Direction direction, ArticleFilterDto articleFilter
    ) {
        return getPageByUsername(username, 0, 10, direction, articleFilter);
    }

    public Page<ArticleResponseDto> getPageByUsername(
            String username, int pageNum, int size, Sort.Direction direction, ArticleFilterDto articleFilter
    ) {
        return articleModuleService.getPageByUsername(
                username,
                pageNum,
                size,
                direction,
                "id",
                ArticleFilterDto.to(articleFilter)
        ).map(ArticleResponseDto::to);
    }

    public Page<ArticleResponseDto> getPageByUsername(
            String username,
            int pageNum,
            int size,
            Sort.Direction direction,
            String orderProperties,
            ArticleFilterDto articleFilter
    ) {
        return articleModuleService.getPageByUsername(
                username,
                pageNum,
                size,
                direction,
                orderProperties,
                ArticleFilterDto.to(articleFilter)
        ).map(ArticleResponseDto::to);
    }

    public Page<ArticleResponseDto> getPage(
            int pageNum, int size, Sort.Direction direction, ArticleFilterDto articleFilter
    ) {
        return articleModuleService.getPage(
                pageNum,
                size,
                direction,
                "id",
                ArticleFilterDto.to(articleFilter)
        ).map(ArticleResponseDto::to);
    }

    public Page<ArticleResponseDto> getPage(
            int pageNum, int size, Sort.Direction direction, String orderProperties, ArticleFilterDto articleFilter
    ) {
        return articleModuleService.getPage(
                pageNum,
                size,
                direction,
                orderProperties,
                ArticleFilterDto.to(articleFilter)
        ).map(ArticleResponseDto::to);
    }

    public void deleteById(Long id) {
        articleModuleService.deleteById(id);
    }

    public void deleteByApiId(String apiId) {
        articleModuleService.deleteByApiId(apiId);
    }

    /**
     * 유저 호출에 의한 삭제를 할 경우 이 메소드를 실행할 것!
     * 현재 호출 한 유저가 게시글 작성자인지, 아니면 어드민 권한을 가지고 있는지 확인 후 삭제 기능 수행
     *
     * @param apiId 해당 게시글의 apiId(uuid)
     */
    public void delete(String apiId) {
        articleModuleService.delete(apiId);
    }

    /**
     * 테스트용 더미 게시글 생성 메소드
     *
     * @param count 게시글 생성 수, 최소 1이상 이어야 동작
     * @return
     */
    public List<ArticleResponseDto> saveDummyArticles(Integer count) {
        return articleModuleService.saveDummyArticles(count).stream().map(ArticleResponseDto::to).toList();
    }
}
