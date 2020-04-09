package com.github.prgrms.social.api.controller.post;

import com.github.prgrms.social.api.model.api.request.post.PostingRequest;
import com.github.prgrms.social.api.model.api.response.ApiResult;
import com.github.prgrms.social.api.model.api.response.post.PostResponse;
import com.github.prgrms.social.api.security.JwtAuthentication;
import com.github.prgrms.social.api.service.post.HashTagService;
import com.github.prgrms.social.api.service.post.PostService;
import com.github.prgrms.social.api.service.user.UserService;
import com.github.prgrms.social.api.util.DtoUtils;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import springfox.documentation.annotations.ApiIgnore;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static com.github.prgrms.social.api.model.api.response.ApiResult.OK;

@RestController
@RequestMapping("api")
@RequiredArgsConstructor
@Api(tags = "포스팅 APIs")
public class PostRestController {

    private final DtoUtils dtoUtils;

    private final UserService userService;

    private final PostService postService;

    private final HashTagService hashTagService;

    @GetMapping(path = "user/{userId}/post/list")
    @ApiOperation(value = "특정 유저 포스트 목록 조회")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "lastId", dataType = "integer", paramType = "query", defaultValue = "0", value = "마지막 포스트 PK(0이면 처음조회)"),
            @ApiImplicitParam(name = "size", dataType = "integer", paramType = "query", defaultValue = "4", value = "최대 조회 갯수")
    })
    public ApiResult<List<PostResponse>> posts(
            @AuthenticationPrincipal JwtAuthentication authentication,
            @ApiParam(value = "공개 유저 PK(비공개 유저인 경우 팔로워만 가능)", example = "1", required = true) @PathVariable Long userId,
            @RequestParam Long lastId,
            @ApiIgnore Pageable pageable
    ) {
        return OK(
                postService.getPostsWithImageAndLikeWithComment(authentication.id.getValue(), userId, lastId, pageable)
                        .stream()
                        .map(dtoUtils::convertPostResponse)
                        .collect(Collectors.toList())
        );
    }


    @GetMapping(path = "user/me/post/list")
    @ApiOperation(value = "내 포스트 목록 조회")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "lastId", dataType = "integer", paramType = "query", defaultValue = "0", value = "마지막 포스트 PK(0이면 처음조회)"),
            @ApiImplicitParam(name = "size", dataType = "integer", paramType = "query", defaultValue = "4", value = "최대 조회 갯수")
    })
    public ApiResult<List<PostResponse>> myPosts(
            @AuthenticationPrincipal JwtAuthentication authentication,
            @RequestParam Long lastId,
            @ApiIgnore Pageable pageable
    ) {
        return OK(
                postService.getPostsWithImageAndLikeWithComment(authentication.id.getValue(), authentication.id.getValue(), lastId, pageable)
                        .stream()
                        .map(dtoUtils::convertPostResponse)
                        .collect(Collectors.toList())
        );
    }


    @GetMapping(path = "user/post/list")
    @ApiOperation(value = "전체 포스트 목록 조회  (API 토큰 필요없음)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "lastId", dataType = "integer", paramType = "query", defaultValue = "0", value = "마지막 포스트 PK(0이면 처음조회)"),
            @ApiImplicitParam(name = "size", dataType = "integer", paramType = "query", defaultValue = "4", value = "최대 조회 갯수")
    })
    public ApiResult<List<PostResponse>> postAll(
            @ApiIgnore Pageable pageable,
            @RequestParam Long lastId
    ) {

        // TODO : 로그인했을 경우 팔로잉 유저것도 가져올 것인지

        return OK(postService.getPostsWithImageAndLikeWithComment(lastId, pageable)
                .stream()
                .map(dtoUtils::convertPostResponse)
                .collect(Collectors.toList())
        );
    }


    @GetMapping(path = "/post/{tag}/list")
    @ApiOperation(value = "특정 해시태그의 포스트 목록 조회  (API 토큰 필요없음)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "lastId", dataType = "integer", paramType = "query", defaultValue = "0", value = "마지막 포스트 PK(0이면 처음조회)"),
            @ApiImplicitParam(name = "size", dataType = "integer", paramType = "query", defaultValue = "4", value = "최대 조회 갯수")
    })
    public ApiResult<List<PostResponse>> postsOfHashTag(
            @ApiParam(value = "해시태그", example = "1", required = true) @PathVariable String tag,
            @RequestParam Long lastId,
            @ApiIgnore Pageable pageable
    ) {
        return OK(hashTagService.findByHashTag(tag, lastId, pageable)
                .stream()
                .map(dtoUtils::convertPostResponse)
                .collect(Collectors.toList())
        );
    }


    @PostMapping(path = "post")
    @ApiOperation(value = "포스트 작성")
    public ApiResult<PostResponse> posting(
            @AuthenticationPrincipal JwtAuthentication authentication,
            @RequestBody PostingRequest request
    ) {
        return OK(dtoUtils.convertPostResponse(postService.write(request.newPost(), authentication.id.getValue(), request.getImagePaths())));
    }

    @PutMapping(path = "post/{postId}")
    @ApiOperation(value = "포스트 수정")
    public ApiResult<PostResponse> editPost(
            @AuthenticationPrincipal JwtAuthentication authentication,
            @ApiParam(value = "대상 포스트 PK",example = "1",required = true) @PathVariable Long postId,
            @RequestBody PostingRequest request
    ) {


        return OK(dtoUtils.convertPostResponse(postService.updatePost(postId,request)));
    }




    @PostMapping(path = "post/images", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ApiOperation(value = "포스트 이미지 업로드")
    public ApiResult<List<String>> images(
            @AuthenticationPrincipal JwtAuthentication authentication,
            @RequestPart  MultipartFile[] images,
            @ApiIgnore MultipartHttpServletRequest request
    ) throws IOException {

        return OK(postService.uploadImage(images,request.getServletContext().getRealPath("/")));
    }


    @PostMapping(path = "retweet/post/{postId}")
    @ApiOperation(value = "리트윗")
    public ApiResult<PostResponse> retweet(
            @AuthenticationPrincipal JwtAuthentication authentication,
            @ApiParam(value = "대상 포스트 PK", example = "1", required = true) @PathVariable Long postId
    ) {
        return OK(dtoUtils.convertPostResponse(postService.retweet(postId,authentication.id.getValue())));
    }


    @PatchMapping(path = "user/{userId}/post/{postId}/like")
    @ApiOperation(value = "포스트 좋아요")
    public ApiResult<PostResponse> like(
            @AuthenticationPrincipal JwtAuthentication authentication,
            @ApiParam(value = "공개 유저 PK(비공개 유저인 경우 팔로워만 가능)", example = "1") @PathVariable Long userId,
            @ApiParam(value = "대상 포스트 PK", example = "1", required = true) @PathVariable Long postId
    ) {
        return OK(dtoUtils.convertPostResponse(postService.like(postId, authentication.id.getValue(),userId)));
    }


    @DeleteMapping(path = "/post/{postId}")
    @ApiOperation(value = "특정 포스트 삭제")
    public ApiResult<Long> removePost(
            @AuthenticationPrincipal JwtAuthentication authentication,
            @ApiParam(value = "대상 포스트 PK", example = "1", required = true) @PathVariable Long postId
    ) {
        return OK(postService.removePost(authentication.id.getValue(), postId));
    }


    @DeleteMapping(path = "user/{userId}/post/{postId}/unlike")
    @ApiOperation(value = "포스트 좋아요 취소")
    public ApiResult<PostResponse> unlike(
            @AuthenticationPrincipal JwtAuthentication authentication,
            @ApiParam(value = "공개 유저 PK(비공개 유저인 경우 팔로워만 가능)", example = "1", required = true) @PathVariable Long userId,
            @ApiParam(value = "대상 포스트 PK", example = "1", required = true) @PathVariable Long postId
    ) {
        return OK(dtoUtils.convertPostResponse(postService.unlike(postId, authentication.id.getValue(),userId)));
    }

}
