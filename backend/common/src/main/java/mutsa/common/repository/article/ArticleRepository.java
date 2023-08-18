/**
 * @project backend
 * @author ARA
 * @since 2023-08-16 AM 11:45
 */

package mutsa.common.repository.article;

import mutsa.common.domain.models.article.Article;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ArticleRepository extends JpaRepository<Article, Long>, ArticleRepositoryCustom {
    Optional<Article> getByApiId(String apiId);

    Optional<Article> findByApiId(String apiId);

    List<Article> findAllByUser_username(String userApiId);

    void deleteByApiId(String apiId);
}
