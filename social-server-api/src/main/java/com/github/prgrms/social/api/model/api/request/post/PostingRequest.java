package com.github.prgrms.social.api.model.api.request.post;

import com.github.prgrms.social.api.model.post.Post;
import com.github.prgrms.social.api.model.post.Writer;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

// 포스트 작성 요청 데이터를 받는 DTO
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class PostingRequest {

    @ApiModelProperty(value = "포스트 내용", required = true)
    private String contents;

    public Post newPost() {
        return Post.builder()
                .contents(contents)
                .build();
    }
/*
    protected PostingRequest() {}

    public String getContents() {
        return contents;
    }



    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("contents", contents)
                .toString();
    }*/

}
