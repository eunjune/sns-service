package com.github.prgrms.social.api.service.post;

import com.github.prgrms.social.api.model.post.Post;
import com.github.prgrms.social.api.repository.post.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

@Service
@RequiredArgsConstructor
public class HashTagService {

    private final PostRepository postRepository;

    @Transactional(readOnly = true)
    public List<Post> findByHashTag(String tag, Long lastId, Pageable pageable) {
        checkNotNull(tag, "tag must be provided.");

        if(lastId == 0L) {
            lastId = postRepository.findFirstByOrderByIdDesc()
                        .map(postProjection -> postProjection.getId() + 1L)
                        .orElse(1L);
        }

        return postRepository.findWithHashtagByName(tag,lastId,pageable);
    }
}
