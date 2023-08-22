package mutsa.common.repository.order;

import mutsa.common.domain.models.article.Article;
import mutsa.common.domain.models.order.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long>,OrderRepositoryCustom {
    Optional<Order> findByApiId(String uuid);

    Page<Order> findByArticle(Article article, Pageable pageable);

    @Query(value = "select * from `order` where order_id = ?1", nativeQuery = true)
    Optional<Order> findByWithDelete(Long id); //soft delete 확인용


}
