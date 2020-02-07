package com.github.prgrms.social.api.controller.post;

import com.github.prgrms.social.api.error.NotFoundException;
import com.github.prgrms.social.api.model.api.request.post.CommentRequest;
import com.github.prgrms.social.api.model.api.request.post.PostingRequest;
import com.github.prgrms.social.api.model.api.response.ApiResult;
import com.github.prgrms.social.api.model.post.Comment;
import com.github.prgrms.social.api.model.post.Post;
import com.github.prgrms.social.api.security.JwtAuthentication;
import com.github.prgrms.social.api.service.post.CommentService;
import com.github.prgrms.social.api.service.post.PostService;
import io.swagger.annotations.*;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.github.prgrms.social.api.model.api.response.ApiResult.OK;

@RestController
@RequestMapping("api")
@Api(tags = "포스팅 APIs")
public class PostRestController {

    private final PostService postService;

    private final CommentService commentService;

    public PostRestController(PostService postService, CommentService commentService) {
        this.postService = postService;
        this.commentService = commentService;

    }

    @PostMapping(path = "post")
    @ApiOperation(value = "포스트 작성")
    public ApiResult<Post> posting(
            @AuthenticationPrincipal JwtAuthentication authentication,
            @RequestBody PostingRequest request
    ) {
        return OK(postService.write(request.newPost(), authentication.id.getValue()));
    }

    @GetMapping(path = "user/{userId}/post/list")
    @ApiOperation(value = "포스트 목록 조회")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "offset", dataType = "integer", paramType = "query", defaultValue = "0", value = "페이징 offset"),
            @ApiImplicitParam(name = "limit", dataType = "integer", paramType = "query", defaultValue = "20", value = "최대 조회 갯수")
    })
    public ApiResult<List<Post>> posts(
            @AuthenticationPrincipal JwtAuthentication authentication,
            @PathVariable
            @ApiParam(value = "조회대상자 PK (본인 또는 친구)", example = "1")
            Long userId,
            Pageable pageable
    ) {
        return OK(postService.findAll(authentication.id.getValue(), userId, pageable));
    }

    @PatchMapping(path = "user/{userId}/post/{postId}/like")
    @ApiOperation(value = "포스트 좋아요")
    public ApiResult<Post> like(
            @AuthenticationPrincipal JwtAuthentication authentication,
            @PathVariable
            @ApiParam(value = "조회대상자 PK (본인 또는 친구)", example = "1")
            Long userId,
            @PathVariable
            @ApiParam(value = "대상 포스트 PK", example = "1")
            Long postId
    ) {
        // TODO query parameter에 offset, limit 파라미터를 추가하고 페이징 처리한다.

        return OK(postService.like(postId, authentication.id.getValue(),userId)
                .orElseThrow(() -> new NotFoundException(Post.class, postId)));
    }

    @PostMapping(path = "user/{userId}/post/{postId}/comment")
    public ApiResult<Comment> comment(
            @AuthenticationPrincipal JwtAuthentication authentication,
            @PathVariable
            @ApiParam(value = "조회대상자 PK (본인 또는 친구)", example = "1")
            Long userId,
            @PathVariable
            @ApiParam(value = "대상 포스트 PK", example = "1")
            Long postId,
            @RequestBody CommentRequest request
    ) {
        // TODO Comment 작성 API를 구현하세요.
        Comment comment = request.newComment();

        return OK(commentService.write(postId, authentication.id.getValue(), userId, comment));
    }

    @GetMapping(path = "user/{userId}/post/{postId}/comment/list")
    public ApiResult<List<Comment>> comments(
            @AuthenticationPrincipal JwtAuthentication authentication,
            @PathVariable
            @ApiParam(value = "조회대상자 PK (본인 또는 친구)", example = "1")
            Long userId,
            @PathVariable
            @ApiParam(value = "대상 포스트 PK", example = "1")
            Long postId
    ) {
        // TODO Comment 목록 조회 API를 구현하세요.
        return OK(commentService.findAll(postId, authentication.id.getValue(), userId));
    }

}