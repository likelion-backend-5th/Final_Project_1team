/**
 * @project backend
 * @author ARA
 * @since 2023-08-18 AM 11:06
 */

package mutsa.api.util;

import lombok.RequiredArgsConstructor;
import mutsa.api.dto.article.ArticleUpdateRequestDto;
import mutsa.common.domain.models.article.Article;
import mutsa.common.domain.models.user.RoleStatus;
import mutsa.common.domain.models.user.User;
import mutsa.common.exception.BusinessException;
import mutsa.common.exception.ErrorCode;
import mutsa.common.repository.article.ArticleRepository;
import mutsa.common.repository.user.UserRepository;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
@RequiredArgsConstructor
public class ArticleUtil {
    private final UserRepository userRepository;
    private final ArticleRepository articleRepository;

    public void isValidUser() {
        if (!StringUtils.hasText(SecurityUtil.getCurrentUsername()) || SecurityUtil.getCurrentUsername().equals("anonymousUser")) {
            throw new BusinessException(
                    "현재 로그인 하고 있지 않습니다.\r\n로그인 된 상태에서 다시 시도해주세요.",
                    ErrorCode.SECURITY_CONTEXT_ERROR
            );
        }
    }

    public User getUserFromSecurityContext() {
        return userRepository.findByUsername(SecurityUtil.getCurrentUsername())
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
    }

    private Article getArticleByApiId(String apiId) {
        return articleRepository.getByApiId(apiId)
                .orElseThrow(() -> new BusinessException(ErrorCode.ARTICLE_NOT_FOUND));
    }

    public Article validArticleAuthor(ArticleUpdateRequestDto requestDto) {
        User curUser = getUserFromSecurityContext();
        Article article = getArticleByApiId(requestDto.getApiId());

        //  현재 로그인 한 유저가 어드민 권한일 경우 인증 통과
        if (curUser.hasRole(RoleStatus.ROLE_ADMIN)) {
            return article;
        }

        //  게시글을 작성한 유저일 경우 통과
        article.validUser(curUser);

        return article;
    }

    public Article validArticleAuthor(String apiId) {
        User curUser = getUserFromSecurityContext();
        Article article = getArticleByApiId(apiId);

        if (curUser.hasRole(RoleStatus.ROLE_ADMIN)) {
            return article;
        }

        article.validUser(curUser);

        return article;
    }
}
