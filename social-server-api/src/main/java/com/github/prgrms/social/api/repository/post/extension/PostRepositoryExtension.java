package com.github.prgrms.social.api.repository.post.extension;

import com.github.prgrms.social.api.model.post.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface PostRepositoryExtension {

    Page<Post> findByKeyword(String keyword, Pageable pageable);

}
