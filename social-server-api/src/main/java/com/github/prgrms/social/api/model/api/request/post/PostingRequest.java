package com.github.prgrms.social.api.model.api.request.post;

import com.github.prgrms.social.api.model.post.Post;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

// 포스트 작성 요청 데이터를 받는 DTO
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class PostingRequest {

    @ApiModelProperty(value = "포스트 내용", required = true)
    private String content;

    @ApiModelProperty(value = "포스트 이미지", required = true)
    private List<String> imagePaths;

    public Post newPost() {
        return Post.builder()
                .content(content)
                .build();
    }
}
