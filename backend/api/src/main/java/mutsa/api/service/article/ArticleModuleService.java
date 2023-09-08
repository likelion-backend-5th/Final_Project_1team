/**
 * @project backend
 * @author ARA
 * @since 2023-08-16 PM 1:19
 */

package mutsa.api.service.article;

import java.util.Collection;
import lombok.RequiredArgsConstructor;
import mutsa.api.dto.article.ArticleCreateRequestDto;
import mutsa.api.dto.article.ArticleUpdateRequestDto;
import mutsa.api.util.ArticleUtil;
import mutsa.api.util.SecurityUtil;
import mutsa.common.domain.filter.article.ArticleFilter;
import mutsa.common.domain.models.article.Article;
import mutsa.common.domain.models.image.Image;
import mutsa.common.domain.models.user.User;
import mutsa.common.exception.BusinessException;
import mutsa.common.repository.article.ArticleRepository;
import mutsa.common.repository.user.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import static mutsa.common.exception.ErrorCode.ARTICLE_NOT_FOUND;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ArticleModuleService {
    private final UserRepository userRepository;
    private final ArticleRepository articleRepository;
    private final PasswordEncoder passwordEncoder;
    private final ArticleUtil articleUtil;

    @Transactional
    public Article save(ArticleCreateRequestDto requestDto) {
        articleUtil.isValidUser();

        Article entity = dtoToEntity(requestDto);
        entity.setUser(articleUtil.getUserFromSecurityContext());

        return articleRepository.save(entity);
    }

    @Transactional
    public Article update(ArticleUpdateRequestDto updateDto) {
        Article article = articleUtil.validArticleAuthor(updateDto);

        article.setTitle(updateDto.getTitle());
        article.setDescription(updateDto.getDescription());
        article.setArticleStatus(updateDto.getArticleStatus());
        article.setPrice(updateDto.getPrice() < 0 ? 0 : updateDto.getPrice());

        return article;
    }

    @Transactional
    public Article setImages(Article article, Collection<Image> imageCollection) {
        article.addImages(imageCollection);

        return articleRepository.save(article);
    }

    @Transactional
    public Article saveTest(ArticleCreateRequestDto requestDto) {
        return articleRepository.save(dtoToEntity(requestDto));
    }

    public Article dtoToEntity(ArticleCreateRequestDto requestDto) {
        return Article.builder()
                .title(requestDto.getTitle())
                .description(requestDto.getDescription())
                .price(requestDto.getPrice() < 0 ? 0 : requestDto.getPrice())
                .build();
    }

    public Article updateDtoToEntity(ArticleUpdateRequestDto updateDto) {
        return articleRepository.getByApiId(updateDto.getApiId())
                .orElseThrow(() -> new BusinessException(ARTICLE_NOT_FOUND));
    }

    @Transactional(readOnly = true)
    public Article getByApiId(String apiId) {
        return articleRepository.findByApiId(apiId)
                .orElseThrow(() -> new BusinessException(ARTICLE_NOT_FOUND));
    }

    @Transactional(readOnly = true)
    public Article getById(Long id) {
        return articleRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ARTICLE_NOT_FOUND));
    }

    @Transactional
    public void deleteById(Long id) {
        articleRepository.deleteById(id);
    }

    @Transactional
    public void deleteByApiId(String apiId) {
        articleRepository.deleteByApiId(apiId);
    }

    /**
     * 유저 호출에 의한 삭제를 할 경우 이 메소드를 실행할 것!
     * 현재 로그인한 유저의 게시글인지 조회한 이후에, 삭제 수행
     *
     * @param apiId 해당 게시글의 apiId(uuid)
     */
    @Transactional
    public void delete(String apiId) {
        articleRepository.delete(articleUtil.validArticleAuthor(apiId));
    }

    @Transactional(readOnly = true)
    public Page<Article> getPageByUsername(
            String username,
            int pageNum,
            int size,
            Sort.Direction direction,
            String orderProperties,
            ArticleFilter articleFilter
    ) {
        Pageable pageable = PageRequest.of(Math.max(0, pageNum), size, direction, orderProperties);

        return articleRepository.getPageByUsername(
                username,
                articleFilter,
                pageable
        );
    }

    @Transactional(readOnly = true)
    public Page<Article> getPage(int pageNum, int size, Sort.Direction direction, String orderProperties, ArticleFilter articleFilter) {
        Pageable pageable = PageRequest.of(Math.max(0, pageNum), size, direction, orderProperties);

        return articleRepository.getPage(articleFilter, pageable);
    }

    @Transactional
    public Article deleteImages(Article article) {
        article.setImages(new ArrayList<>());
        return articleRepository.save(article);
    }
}
