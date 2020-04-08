package com.github.prgrms.social.api.service.post;

import com.github.prgrms.social.api.error.NotFoundException;
import com.github.prgrms.social.api.model.post.HashTag;
import com.github.prgrms.social.api.model.post.Post;
import com.github.prgrms.social.api.repository.post.HashTagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

@Service
@RequiredArgsConstructor
public class HashTagService {

    private final HashTagRepository hashTagRepository;


    // TODO : 더 효율적인 방법이 있을지 고민
    @Transactional(readOnly = true)
    public List<Post> findByHashTag(String tag, Long lastId, Pageable pageable) {
        checkNotNull(tag, "tag must be provided.");
        checkNotNull(lastId, "lastId must be provided.");

        if(lastId == 0L) {
            return hashTagRepository.findByName(tag)
                    .map(hashTag -> {
                        List<Post> posts = new ArrayList<>(hashTag.getPosts());
                        posts.sort((o1, o2) -> o2.getId().compareTo(o1.getId()));
                        return posts.subList(0, Math.min(pageable.getPageSize(), posts.size()));
                    })
                    .orElseThrow(() -> new NotFoundException(HashTag.class, tag));
        }

        return hashTagRepository.findByName(tag)
                .map(hashTag -> {
                    List<Post> posts = new ArrayList<>(hashTag.getPosts());
                    posts.sort((o1, o2) -> o2.getId().compareTo(o1.getId()));

                    int startIndex = 0;
                    for(int i=0; i<posts.size(); ++i) {
                        if(posts.get(i).getId().equals(lastId)) {
                            startIndex = i + 1;
                            break;
                        }
                    }

                    int size = posts.size() - startIndex < pageable.getPageSize() ?
                            posts.size() :
                            pageable.getPageSize() + startIndex;

                    return posts.subList(startIndex, size);
                })
                .orElseThrow(() -> new NotFoundException(HashTag.class, tag));
    }
}
