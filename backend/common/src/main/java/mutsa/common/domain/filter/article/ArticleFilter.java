/**
 * @project backend
 * @author ARA
 * @since 2023-08-19 PM 5:09
 */

package mutsa.common.domain.filter.article;

import lombok.*;
import mutsa.common.domain.models.Status;
import mutsa.common.domain.models.article.ArticleStatus;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class ArticleFilter {
    private ArticleStatus articleStatus = ArticleStatus.LIVE;
    private Status status = Status.ACTIVE;

    public static ArticleFilter of() {
        return new ArticleFilter();
    }

    public static ArticleFilter of(Status status, ArticleStatus articleStatus) {
        ArticleFilter articleFilter = of();

        articleFilter.setStatus(status);
        articleFilter.setArticleStatus(articleStatus);

        return articleFilter;
    }
}
