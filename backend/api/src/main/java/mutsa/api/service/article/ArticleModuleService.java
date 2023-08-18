/**
 * @project backend
 * @author ARA
 * @since 2023-08-16 PM 1:19
 */

package mutsa.api.service.article;

import lombok.RequiredArgsConstructor;
import mutsa.api.dto.article.ArticleCreateRequestDto;
import mutsa.api.dto.article.ArticleUpdateRequestDto;
import mutsa.api.util.ArticleUtil;
import mutsa.api.util.SecurityUtil;
import mutsa.common.domain.models.article.Article;
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
import static mutsa.common.exception.ErrorCode.USER_NOT_FOUND;

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
        return articleRepository.save(dtoToEntity(requestDto));
    }

    @Transactional
    public Article update(ArticleUpdateRequestDto updateDto) {
        Article article = articleUtil.validArticleAuthor(updateDto);

        article.setTitle(updateDto.getTitle());
        article.setDescription(updateDto.getDescription());

        return article;
    }

    @Transactional
    public Article updateTest(ArticleUpdateRequestDto updateDto) {
        Article article = articleRepository.getByApiId(updateDto.getApiId())
                .orElseThrow(() -> new BusinessException(ARTICLE_NOT_FOUND));

        article.setTitle(updateDto.getTitle());
        article.setDescription(updateDto.getDescription());

        return article;
    }

    public Article dtoToEntity(ArticleCreateRequestDto requestDto) {
        return Article.builder()
                .title(requestDto.getTitle())
                .description(requestDto.getDescription())
                .user(userRepository.findByUsername(requestDto.getUsername())
                              .orElseThrow(() -> new BusinessException(USER_NOT_FOUND))
                )
                .build();
    }

    public Article updateToEntity(ArticleUpdateRequestDto updateDto) {
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
     * 현재 호출 한 유저가 게시글 작성자인지, 아니면 어드민 권한을 가지고 있는지 확인 후 삭제 기능 수행
     *
     * @param apiId 해당 게시글의 apiId(uuid)
     */
    @Transactional
    public void delete(String apiId) {
        articleRepository.delete(articleUtil.validArticleAuthor(apiId));
    }

    @Transactional(readOnly = true)
    public List<Article> getsByUserApiId(String userApiId) {
        List<Article> articles = articleRepository.findAllByUser_username(userApiId);

        if (articles == null || articles.isEmpty()) {
            throw new BusinessException(ARTICLE_NOT_FOUND);
        }

        return articles;
    }

    @Transactional(readOnly = true)
    public Page<Article> getPageByUsername(
            String username,
            int pageNum,
            int size,
            Sort.Direction direction,
            String orderProperties
    ) {
        Pageable pageable = PageRequest.of(Math.max(0, pageNum - 1), size, direction, orderProperties);

        return articleRepository.getPageByUsername(username, pageable);
    }

    @Transactional(readOnly = true)
    public Page<Article> getPage(int pageNum, int size, Sort.Direction direction, String orderProperties) {
        Pageable pageable = PageRequest.of(Math.max(0, pageNum - 1), size, direction, orderProperties);

        return articleRepository.getPage(pageable);
    }

    /**
     * 테스트용 더미 게시글 생성 메소드
     * @param count
     * 게시글 생성 수, 최소 1이상 이어야 동작
     * @return
     */
    @Transactional
    public List<Article> saveDummyArticles(Integer count) {
        String username = SecurityUtil.getCurrentUsername();

        if (username == null || username.isBlank() || username.equals("anonymousUser")) {
            User testUser = userRepository.findByUsername("testuser").orElse(null);

            if (testUser == null) {
                testUser = User.of("testuser", passwordEncoder.encode("test"), "test@gmail.com", null, null, null);
                testUser = userRepository.save(testUser);
            }
            username = testUser.getUsername();
        }

        ArticleCreateRequestDto requestDto;
        List<Article> articles = new ArrayList<>();

        for (Integer i = 0; i < count; i++) {
            requestDto = new ArticleCreateRequestDto();
            requestDto.setTitle("Article-" + (i+1));
            requestDto.setDescription("Random Content-" + (i + 1));
            requestDto.setUsername(username);

            articles.add(save(requestDto));
        }

        return articles;
    }
}
