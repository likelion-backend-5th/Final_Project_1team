/**
 * @project backend
 * @author ARA
 * @since 2023-08-19 PM 5:09
 */

package mutsa.common.domain.filter.article;

import lombok.*;
import mutsa.common.domain.models.Status;
import mutsa.common.domain.models.article.ArticleStatus;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class ArticleFilter {
    private ArticleStatus articleStatus = ArticleStatus.LIVE;
    private Status status = Status.ACTIVE;
    private String title = null;
    private String description = null;
    private String username = null;

    public static ArticleFilter of() {
        return new ArticleFilter();
    }

    public static ArticleFilter of(Status status, ArticleStatus articleStatus) {
        ArticleFilter articleFilter = of();

        articleFilter.setStatus(status);
        articleFilter.setArticleStatus(articleStatus);

        return articleFilter;
    }

    public static ArticleFilter of (Status status, ArticleStatus articleStatus, String title, String description, String username) {
        ArticleFilter articleFilter = of(status, articleStatus);

        articleFilter.setTitle(title);
        articleFilter.setDescription(description);
        articleFilter.setUsername(username);

        return articleFilter;
    }
}
