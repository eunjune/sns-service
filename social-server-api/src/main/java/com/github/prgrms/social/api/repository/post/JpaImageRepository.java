package com.github.prgrms.social.api.repository.post;

import com.github.prgrms.social.api.model.post.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface JpaImageRepository extends JpaRepository<Image,Long> {

    Image save(Image image);

    @Transactional(readOnly = true)
    List<Image> findAll();

}
