/**
 * @project backend
 * @author ARA
 * @since 2023-08-16 PM 1:18
 */

package mutsa.api.service.article;

import lombok.RequiredArgsConstructor;
import mutsa.api.dto.article.ArticleRequestDto;
import mutsa.api.dto.article.ArticleResponseDto;
import mutsa.api.dto.article.ArticleUpdateDto;
import mutsa.common.domain.models.article.Article;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ArticleService {
    private final ArticleModuleService articleModuleService;

    public ArticleResponseDto save(ArticleRequestDto requestDto) {
        return articleModuleService.save(requestDto);
    }

    public ArticleResponseDto update(ArticleUpdateDto updateDto) {
        return articleModuleService.update(updateDto);
    }

    public Article requestDtoToEntity(ArticleRequestDto requestDto) {
        return articleModuleService.dtoToEntity(requestDto);
    }

    public Article getByApiId(String apiId) {
        return articleModuleService.getByApiId(apiId);
    }

    public Article getById(Long id) {
        return articleModuleService.getById(id);
    }
}
