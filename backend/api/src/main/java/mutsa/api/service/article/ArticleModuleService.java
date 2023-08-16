/**
 * @project backend
 * @author ARA
 * @since 2023-08-16 PM 1:19
 */

package mutsa.api.service.article;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import mutsa.api.dto.article.ArticleRequestDto;
import mutsa.api.dto.article.ArticleResponseDto;
import mutsa.api.dto.article.ArticleUpdateDto;
import mutsa.common.domain.models.article.Article;
import mutsa.common.repository.article.ArticleRepository;
import mutsa.common.repository.user.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
@RequiredArgsConstructor
public class ArticleModuleService {
    private final UserRepository userRepository;
    private final ArticleRepository articleRepository;

    @Transactional
    public ArticleResponseDto save(ArticleRequestDto requestDto) {
        return ArticleResponseDto.to(articleRepository.save(dtoToEntity(requestDto)));
    }

    @Transactional
    public ArticleResponseDto update(ArticleUpdateDto updateDto) {
        Article article = articleRepository.getByApiId(updateDto.getApiId());

        article.setTitle(updateDto.getTitle());
        article.setDescription(updateDto.getDescription());

        return ArticleResponseDto.to(article);
    }

    public Article dtoToEntity(ArticleRequestDto requestDto) {
        return Article.builder()
                .title(requestDto.getTitle())
                .description(requestDto.getDescription())
                //  TODO 임시로 null 붙혀둠
                //  TODO 나중에 AOP로 해서 유저 확인하는 코드 작성해두기
                .user(userRepository.findByUsername(requestDto.getUsername()).orElse(null))
                .build();
    }

    public Article updateToEntity(ArticleUpdateDto updateDto) {
        return articleRepository.getByApiId(updateDto.getApiId());
    }


    public Article getByApiId(String apiId) {
        return articleRepository.findByApiId(apiId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "없는 order ApiId"));
    }

    public Article getById(Long id) {
        return articleRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "없는 order ID"));
    }
}
