package com.github.prgrms.social.api.model.api.request.post;

import com.github.prgrms.social.api.model.post.Post;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

// 포스트 작성 요청 데이터를 받는 DTO
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostingRequest {

    @ApiModelProperty(value = "포스트 내용", required = true)
    private String content;

    @ApiModelProperty(value = "포스트 이미지", required = true)
    private Set<String> imagePaths;

    public Post newPost() {
        return Post.builder()
                .content(content)
                .build();
    }
}
