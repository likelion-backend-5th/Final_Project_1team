package mutsa.common.repository.review;

import java.util.Optional;
import mutsa.common.domain.models.review.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    Optional<Review> findByApiId(String reviewApiId);
}
