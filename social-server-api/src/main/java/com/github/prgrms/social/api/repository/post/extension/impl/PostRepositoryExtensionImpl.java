package com.github.prgrms.social.api.repository.post.extension.impl;

import com.github.prgrms.social.api.model.post.Post;
import com.github.prgrms.social.api.model.post.QImage;
import com.github.prgrms.social.api.model.post.QLikeInfo;
import com.github.prgrms.social.api.model.post.QPost;
import com.github.prgrms.social.api.repository.post.extension.PostRepositoryExtension;
import com.querydsl.core.QueryResults;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

public class PostRepositoryExtensionImpl extends QuerydslRepositorySupport implements PostRepositoryExtension {

    public PostRepositoryExtensionImpl() {
        super(Post.class);
    }

    @Override
    public Page<Post> findByKeyword(String keyword, Pageable pageable) {
        QPost post = QPost.post;
        JPQLQuery<Post> query = from(post).where(post.user.isPrivate.isFalse()
                .and(post.content.containsIgnoreCase(keyword)))
                .leftJoin(post.likeInfos, QLikeInfo.likeInfo).fetchJoin()
                .leftJoin(post.images, QImage.image).fetchJoin();

        JPQLQuery<Post> pageableQuery = getQuerydsl().applyPagination(pageable, query);
        QueryResults<Post> fetchResults = pageableQuery.fetchResults();
        return new PageImpl<>(fetchResults.getResults(), pageable, fetchResults.getTotal());
    }
}
