/**
 * @project backend
 * @author ARA
 * @since 2023-08-18 AM 11:06
 */

package mutsa.api.util;

import lombok.RequiredArgsConstructor;
import mutsa.api.dto.article.ArticleCreateRequestDto;
import mutsa.api.dto.article.ArticleUpdateRequestDto;
import mutsa.common.domain.models.article.Article;
import mutsa.common.domain.models.user.RoleStatus;
import mutsa.common.domain.models.user.User;
import mutsa.common.exception.BusinessException;
import mutsa.common.exception.ErrorCode;
import mutsa.common.repository.article.ArticleRepository;
import mutsa.common.repository.user.UserRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ArticleUtil {
    private final UserRepository userRepository;
    private final ArticleRepository articleRepository;

    public Article validArticleAuthor(ArticleUpdateRequestDto requestDto) {
        User curUser = userRepository.findByUsername(SecurityUtil.getCurrentUsername())
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        Article article = articleRepository.getByApiId(requestDto.getApiId())
                .orElseThrow(() -> new BusinessException(ErrorCode.ARTICLE_NOT_FOUND));

        //  현재 로그인 한 유저가 어드민 권한일 경우 인증 통과
        if (curUser.hasRole(RoleStatus.ROLE_ADMIN)) {
            return article;
        }

        //  현재 로그인 한 유저가 요청을 보낸 유저와 같을 경우 통과
        if (!curUser.getUsername().equals(requestDto.getUsername())) {
            throw new BusinessException(ErrorCode.ARTICLE_USER_NOT_MATCH);
        }

        //  게시글을 작성한 유저일 경우 통과
        article.validUser(curUser);

        return article;
    }

    public Article validArticleAuthor(String apiId) {
        User curUser = userRepository.findByUsername(SecurityUtil.getCurrentUsername())
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        Article article = articleRepository.getByApiId(apiId)
                .orElseThrow(() -> new BusinessException(ErrorCode.ARTICLE_NOT_FOUND));

        if (curUser.hasRole(RoleStatus.ROLE_ADMIN)) {
            return article;
        }

        article.validUser(curUser);

        return article;
    }
}
