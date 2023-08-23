package mutsa.common.repository.order;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import mutsa.common.customRepository.Querydsl4RepositorySupport;
import mutsa.common.domain.filter.order.OrderFilter;
import mutsa.common.domain.models.article.Article;
import mutsa.common.domain.models.order.Order;
import mutsa.common.domain.models.order.OrderStatus;
import mutsa.common.domain.models.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;

import java.util.List;

import static mutsa.common.domain.models.order.QOrder.order;

public class OrderRepositoryImpl extends Querydsl4RepositorySupport implements OrderRepositoryCustom {
    public OrderRepositoryImpl() {
        super(Order.class);
    }


    @Override
    public Page<Order> getOrderByFilterBySeller(OrderFilter orderFilter, User user, Pageable pageable) {
        JPAQuery<Order> query = selectFrom(order)
                .where(
                        eqOrderStatus(orderFilter.getOrderStatus()),
                        containsArticleTitle(orderFilter.getText()),
                        eqArticleUserId(user));

        List<Order> orders = this.getQuerydsl().applyPagination(pageable, query).fetch();
        return new PageImpl<>(orders, pageable, query.fetch().size());
    }

    @Override
    public Page<Order> getOrderByFilterByConsumer(OrderFilter orderFilter, User user, Pageable pageable) {
        JPAQuery<Order> query = selectFrom(order)
                .where(
                        eqUsersId(user),
                        eqOrderStatus(orderFilter.getOrderStatus()));
        List<Order> orders = this.getQuerydsl().applyPagination(pageable, query).fetch();
        return new PageImpl<>(orders, pageable, query.fetch().size());
    }

    private BooleanExpression eqArticleUserId(User user) {
        if (user == null) {
            return null;
        }
        return order.article.user.id.eq(user.getId());
    }

    private BooleanExpression containsArticleTitle(String text) {
        if (!StringUtils.hasText(text)) {
            return null;
        }
        return order.article.title.contains(text);
    }

    private BooleanExpression eqUsersId(User user) {
        if (user == null) {
            return null;
        }
        return order.user.id.eq(user.getId());
    }

    private BooleanExpression eqArticleId(Article article) {
        if (article == null) {
            return null;
        }
        return order.article.id.eq(article.getId());
    }

    private BooleanExpression eqOrderStatus(OrderStatus orderStatus) {
        if (orderStatus == null) {
            return null;
        }
        return order.orderStatus.eq(orderStatus);
    }

    private JPAQuery<Order> getQuery() {
        return selectFrom(order);
    }
}
