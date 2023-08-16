/**
 * @project backend
 * @author ARA
 * @since 2023-08-16 AM 11:45
 */

package mutsa.common.repository.article;

import mutsa.common.domain.models.article.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {
    Article getByApiId(String apiId);

    Optional<Article> findByApiId(String apiId);
}
