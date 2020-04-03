package com.github.prgrms.social.api.model.api.response.post;

import com.github.prgrms.social.api.model.post.Post;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MyPostResponse {

    private long totalCount;

    private List<Post> posts;
}
