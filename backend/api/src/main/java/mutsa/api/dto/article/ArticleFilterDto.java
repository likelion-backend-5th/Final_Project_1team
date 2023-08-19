/**
 * @project backend
 * @author ARA
 * @since 2023-08-19 PM 5:32
 */

package mutsa.api.dto.article;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import mutsa.common.domain.filter.article.ArticleFilter;
import mutsa.common.domain.models.Status;
import mutsa.common.domain.models.article.ArticleStatus;

@Getter
@Setter
public class ArticleFilterDto {
    @NotNull
    private ArticleStatus articleStatus;
    @NotNull
    private Status status;
    private String title;
    private String description;
    private String username;

    public static ArticleFilterDto of() {
        ArticleFilterDto articleFilterDto = new ArticleFilterDto();

        articleFilterDto.status = Status.ACTIVE;
        articleFilterDto.articleStatus = ArticleStatus.LIVE;

        return articleFilterDto;
    }

    public static ArticleFilterDto of(Status status, ArticleStatus articleStatus) {
        ArticleFilterDto articleFilterDto = new ArticleFilterDto();

        articleFilterDto.status = status;
        articleFilterDto.articleStatus = articleStatus;

        return articleFilterDto;
    }

    public static ArticleFilter to(ArticleFilterDto filterDto) {
        return ArticleFilter.of(
                filterDto.getStatus(),
                filterDto.getArticleStatus(),
                filterDto.getTitle(),
                filterDto.getDescription(),
                filterDto.getUsername()
        );
    }

    public static ArticleFilterDto of(
            Status status,
            ArticleStatus articleState,
            String title,
            String description,
            String username
    ) {
        ArticleFilterDto articleFilterDto = ArticleFilterDto.of(status, articleState);

        articleFilterDto.setTitle(title);
        articleFilterDto.setDescription(description);
        articleFilterDto.setUsername(username);

        return articleFilterDto;
    }
}
