package com.github.prgrms.social.api.controller.post;

import com.github.prgrms.social.api.model.api.request.post.CommentRequest;
import com.github.prgrms.social.api.model.api.response.ApiResult;
import com.github.prgrms.social.api.model.api.response.post.CommentResponse;
import com.github.prgrms.social.api.model.post.Comment;
import com.github.prgrms.social.api.security.JwtAuthentication;
import com.github.prgrms.social.api.service.post.CommentService;
import com.github.prgrms.social.api.util.DtoUtils;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static com.github.prgrms.social.api.model.api.response.ApiResult.OK;

@Slf4j
@RestController
@RequestMapping("api")
@RequiredArgsConstructor
@Api(tags = "댓글 APIs")
public class CommentRestController {

    private final DtoUtils dtoUtils;

    private final CommentService commentService;

    @GetMapping(path = "user/{userId}/post/{postId}/comment/list")
    @ApiOperation(value = "댓글 조회")
    public ApiResult<List<CommentResponse>> comments(
            @AuthenticationPrincipal JwtAuthentication authentication,
            @ApiParam(value = "공개 유저 PK(비공개 유저인 경우 팔로워만 가능)", example = "1", required = true) @PathVariable Long userId,
            @ApiParam(value = "대상 포스트 PK", example = "1", required = true) @PathVariable Long postId
    ) {
        return OK(
                commentService.findAll(postId, authentication.id.getValue(), userId)
                        .stream()
                        .map(dtoUtils::convertCommentResponse)
                        .collect(Collectors.toList())
        );
    }

    @PostMapping(path = "user/{userId}/post/{postId}/comment")
    @ApiOperation(value = "댓글 작성")
    public ApiResult<CommentResponse> comment(
            @AuthenticationPrincipal JwtAuthentication authentication,
            @ApiParam(value = "공개 유저 PK(비공개 유저인 경우 팔로워만 가능)", example = "1", required = true) @PathVariable Long userId,
            @ApiParam(value = "대상 포스트 PK", example = "1", required = true) @PathVariable Long postId,
            @RequestBody CommentRequest request
    ) {
        Comment comment = request.newComment();

        return OK(dtoUtils.convertCommentResponse(commentService.write(postId, authentication.id.getValue(), userId, comment)));
    }

}
