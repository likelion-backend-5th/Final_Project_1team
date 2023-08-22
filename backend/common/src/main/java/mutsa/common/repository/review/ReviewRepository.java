package mutsa.common.repository.review;

import java.util.Optional;
import mutsa.common.domain.models.Status;
import mutsa.common.domain.models.article.Article;
import mutsa.common.domain.models.review.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    Optional<Review> findByApiId(String reviewApiId);
    Page<Review> findByArticle(Article article, Pageable pageable);

    @Query(value = "select * from review where status = 'DELETED'", nativeQuery = true)
    Optional<Review> findByStatus(Status status);
}
