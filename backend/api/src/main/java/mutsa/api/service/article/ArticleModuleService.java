/**
 * @project backend
 * @author ARA
 * @since 2023-08-16 PM 1:19
 */

package mutsa.api.service.article;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import mutsa.api.dto.article.ArticleCreateRequestDto;
import mutsa.api.dto.article.ArticleUpdateDto;
import mutsa.common.domain.models.article.Article;
import mutsa.common.repository.article.ArticleRepository;
import mutsa.common.repository.user.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ArticleModuleService {
    private final UserRepository userRepository;
    private final ArticleRepository articleRepository;

    @Transactional
    public Article save(ArticleCreateRequestDto requestDto) {
        return articleRepository.save(dtoToEntity(requestDto));
    }

    @Transactional
    public Article update(ArticleUpdateDto updateDto) {
        Article article = articleRepository.getByApiId(updateDto.getApiId()).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "업데이트 하려는 Article은 존재하지 않습니다."
        ));

        article.setTitle(updateDto.getTitle());
        article.setDescription(updateDto.getDescription());

        return article;
    }

    public Article dtoToEntity(ArticleCreateRequestDto requestDto) {
        return Article.builder()
                .title(requestDto.getTitle())
                .description(requestDto.getDescription())
                //  TODO 임시로 null 붙혀둠
                //  TODO 나중에 AOP로 해서 유저 확인하는 코드 작성해두기
                .user(userRepository.findByUsername(requestDto.getUsername()).orElse(null))
                .build();
    }

    public Article updateToEntity(ArticleUpdateDto updateDto) {
        return articleRepository.getByApiId(updateDto.getApiId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "존재하지 않는 게시글을 요청했습니다."));
    }


    public Article getByApiId(String apiId) {
        return articleRepository.findByApiId(apiId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "없는 article ApiId"));
    }

    public Article getById(Long id) {
        return articleRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "없는 article ID"));
    }

    @Transactional
    public void deleteById(Long id) {
        articleRepository.deleteById(id);
    }

    @Transactional
    public void deleteByApiId(String apiId) {
        articleRepository.deleteByApiId(apiId);
    }

    public List<Article> getsByUserApiId(String userApiId) {
        List<Article> articles = articleRepository.findAllByUser_username(userApiId);

        if (articles == null || articles.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 유저가 작성한 게시글은 없습니다.");
        }

        return articles;
    }

    public Page<Article> getPageByUsername(String username, int pageNum, int size, Sort.Direction direction, String orderProperties) {
        Pageable pageable = PageRequest.of(Math.max(0, pageNum - 1), size, direction, orderProperties);

        return articleRepository.getPageByUsername(username, pageable);
    }

    public Page<Article> getPage(int pageNum, int size, Sort.Direction direction, String orderProperties) {
        Pageable pageable = PageRequest.of(Math.max(0, pageNum - 1), size, direction, orderProperties);

        return articleRepository.getPage(pageable);
    }
}
