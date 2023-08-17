/**
 * @project backend
 * @author ARA
 * @since 2023-08-16 PM 1:18
 */

package mutsa.api.service.article;

import lombok.RequiredArgsConstructor;
import mutsa.api.dto.article.ArticleCreateRequestDto;
import mutsa.api.dto.article.ArticleResponseDto;
import mutsa.api.dto.article.ArticleUpdateDto;
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

    public ArticleResponseDto update(ArticleUpdateDto updateDto) {
        return ArticleResponseDto.to(articleModuleService.update(updateDto));
    }

    public Article requestDtoToEntity(ArticleCreateRequestDto requestDto) {
        return articleModuleService.dtoToEntity(requestDto);
    }

    public Article getByApiId(String apiId) {
        return articleModuleService.getByApiId(apiId);
    }

    public Article getById(Long id) {
        return articleModuleService.getById(id);
    }

    public ArticleResponseDto read(String apiId) {
        return ArticleResponseDto.to(articleModuleService.getByApiId(apiId));
    }

    public List<Article> getsByUserApiId(String userApiId) {
        return articleModuleService.getsByUserApiId(userApiId);
    }

    public List<ArticleResponseDto> getPageByUsername(String username, Sort.Direction direction) {
        return getPageByUsername(username, 0, 10, direction);
    }

    public List<ArticleResponseDto> getPageByUsername(
            String username,
            int pageNum,
            int size,
            Sort.Direction direction
    ) {
        Page<Article> page = articleModuleService.getPageByUsername(username, pageNum, size, direction, "id");
        List<ArticleResponseDto> dtoPage = page.getContent().stream().map(ArticleResponseDto::to).toList();

        return articleModuleService.getPageByUsername(username, pageNum, size, direction, "id")
                .getContent()
                .stream()
                .map(ArticleResponseDto::to)
                .toList();
    }

    public List<ArticleResponseDto> getPageByUsername(
            String username,
            int pageNum,
            int size,
            Sort.Direction direction,
            String orderProperties
    ) {
        return articleModuleService.getPageByUsername(username, pageNum, size, direction, orderProperties)
                .getContent()
                .stream()
                .map(ArticleResponseDto::to)
                .toList();
    }

    public List<ArticleResponseDto> getPage(int pageNum, int size, Sort.Direction direction) {
        return articleModuleService.getPage(pageNum, size, direction, "id")
                .getContent()
                .stream()
                .map(ArticleResponseDto::to)
                .toList();
    }

    public List<ArticleResponseDto> getPage(int pageNum, int size, Sort.Direction direction, String orderProperties) {
        return articleModuleService.getPage(pageNum, size, direction, orderProperties)
                .getContent()
                .stream()
                .map(ArticleResponseDto::to)
                .toList();
    }

    public void deleteById(Long id) {
        articleModuleService.deleteById(id);
    }

    public void deleteByApiId(String apiId) {
        articleModuleService.deleteByApiId(apiId);
    }
}
