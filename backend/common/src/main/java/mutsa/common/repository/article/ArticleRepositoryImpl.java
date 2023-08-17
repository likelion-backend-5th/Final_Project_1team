/**
 * @project backend
 * @author ARA
 * @since 2023-08-17 PM 4:33
 */

package mutsa.common.repository.article;

import com.querydsl.jpa.impl.JPAQuery;
import mutsa.common.customRepository.Querydsl4RepositorySupport;
import mutsa.common.domain.models.article.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static mutsa.common.domain.models.article.QArticle.article;

@Repository
@Transactional
public class ArticleRepositoryImpl extends Querydsl4RepositorySupport implements ArticleRepositoryCustom {
    public ArticleRepositoryImpl() {
        super(Article.class);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Article> getPageByUsername(String username, Pageable pageable) {
        JPAQuery<Article> query = selectFrom(article)
                .where(article.user.username.eq(username));

        //  TODO 카운트 쿼리 짜기

        long totalCount = query.fetchCount();
        List<Article> articleList = getQuerydsl().applyPagination(pageable, query).fetch();
        return new PageImpl<Article>(articleList, pageable, totalCount);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Article> getPage(Pageable pageable) {
        JPAQuery<Article> query = selectFrom(article);

        long totalCount = query.fetchCount();
        List<Article> articleList = getQuerydsl().applyPagination(pageable, query).fetch();
        return new PageImpl<Article>(articleList, pageable, totalCount);
    }
}
