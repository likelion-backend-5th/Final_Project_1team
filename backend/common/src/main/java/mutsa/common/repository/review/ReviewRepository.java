package mutsa.common.repository.review;

import java.util.Optional;
import mutsa.common.domain.models.article.Article;
import mutsa.common.domain.models.review.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    Optional<Review> findByApiId(String reviewApiId);
    Page<Review> findByArticle(Article article, Pageable pageable);
}
