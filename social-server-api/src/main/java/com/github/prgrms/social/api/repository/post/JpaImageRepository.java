package com.github.prgrms.social.api.repository.post;

import com.github.prgrms.social.api.model.post.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaImageRepository extends JpaRepository<Image,Long> {

    Image save(Image image);
}
