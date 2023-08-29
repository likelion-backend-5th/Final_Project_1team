package mutsa.common.repository.order;

import com.querydsl.core.QueryResults;
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

        // 사용자가 요청한 pageable 정보를 적용하여 페이징된 결과를 가져옴
        QueryResults<Order> queryResults = this.getQuerydsl().applyPagination(pageable, query).fetchResults();

        return new PageImpl<>(queryResults.getResults(), pageable, queryResults.getTotal());
    }

    @Override
    public Page<Order> getOrderByFilterByConsumer(OrderFilter orderFilter, User user, Pageable pageable) {
        JPAQuery<Order> query = selectFrom(order)
                .where(
                        eqUsersId(user),
                        containsArticleTitle(orderFilter.getText()),
                        eqOrderStatus(orderFilter.getOrderStatus()));

        // 사용자가 요청한 pageable 정보를 적용하여 페이징된 결과를 가져옴
        QueryResults<Order> queryResults = this.getQuerydsl().applyPagination(pageable, query).fetchResults();

        // 가져온 페이징된 결과와 pageable 정보를 사용하여 Page 객체를 생성하여 반환
        return new PageImpl<>(queryResults.getResults(), pageable, queryResults.getTotal());
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
