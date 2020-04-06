package com.github.prgrms.social.api.controller.post;

import com.github.prgrms.social.api.model.api.request.post.PostingRequest;
import com.github.prgrms.social.api.model.api.response.ApiResult;
import com.github.prgrms.social.api.model.api.response.post.PostResponse;
import com.github.prgrms.social.api.security.JwtAuthentication;
import com.github.prgrms.social.api.service.post.HashTagService;
import com.github.prgrms.social.api.service.post.PostService;
import com.github.prgrms.social.api.util.DtoUtils;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

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

    private final PostService postService;

    private final HashTagService hashTagService;

    @GetMapping(path = "user/{userId}/post/list")
    @ApiOperation(value = "특정 유저 포스트 목록 조회")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "lastId", dataType = "integer", paramType = "query", defaultValue = "0", value = "마지막 포스트의 아이디"),
            @ApiImplicitParam(name = "size", dataType = "integer", paramType = "query", defaultValue = "20", value = "최대 조회 갯수")
    })
    public ApiResult<List<PostResponse>> posts(
            @AuthenticationPrincipal JwtAuthentication authentication,
            @PathVariable(required = false)
            @ApiParam(value = "조회대상자 PK (본인 또는 친구)", example = "1")
            Long userId,
            @RequestParam
            Long lastId,
            Pageable pageable
    ) {
        return OK(
                postService.findByUserId(authentication.id.getValue(), userId, lastId, pageable)
                        .stream()
                        .map(dtoUtils::convertPostResponse)
                        .collect(Collectors.toList())
        );
    }


    @GetMapping(path = "user/me/post/list")
    @ApiOperation(value = "내 포스트 목록 조회")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "lastId", dataType = "integer", paramType = "query", defaultValue = "0", value = "마지막 포스트의 아이디"),
            @ApiImplicitParam(name = "size", dataType = "integer", paramType = "query", defaultValue = "20", value = "최대 조회 갯수")
    })
    public ApiResult<List<PostResponse>> myPosts(
            @AuthenticationPrincipal JwtAuthentication authentication,
            @RequestParam
            Long lastId,
            Pageable pageable
    ) {
        return OK(
                postService.findByUserId(authentication.id.getValue(), authentication.id.getValue(), lastId, pageable)
                        .stream()
                        .map(dtoUtils::convertPostResponse)
                        .collect(Collectors.toList())
        );
    }


    @GetMapping(path = "user/post/list")
    @ApiOperation(value = "전체 포스트 목록 조회")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "lastId", dataType = "integer", paramType = "query", defaultValue = "0", value = "마지막 포스트의 아이디"),
            @ApiImplicitParam(name = "size", dataType = "integer", paramType = "query", defaultValue = "3", value = "최대 조회 갯수")
    })
    public ApiResult<List<PostResponse>> postAll(
            @AuthenticationPrincipal JwtAuthentication authentication,
            Pageable pageable,
            @RequestParam Long lastId) {

        // TODO : 로그인했을 경우 팔로잉 유저것도 가져올 것인지

        return OK(postService.findAll(lastId, pageable)
                .stream()
                .map(dtoUtils::convertPostResponse)
                .collect(Collectors.toList())
        );
    }


    @GetMapping(path = "/post/{tag}/list")
    @ApiOperation(value = "특정 해시태그의 포스트 목록 조회")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "size", dataType = "integer", paramType = "query", defaultValue = "3", value = "최대 조회 갯수"),
            @ApiImplicitParam(name = "size", dataType = "integer", paramType = "query", defaultValue = "20", value = "최대 조회 갯수")
    })
    public ApiResult<List<PostResponse>> postsOfHashTag(
            @PathVariable
            @ApiParam(value = "해시태그", example = "1")
            String tag,
            @RequestParam
            Long lastId,
            Pageable pageable
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
            @PathVariable Long postId,
            @RequestBody PostingRequest request
    ) {
        return OK(dtoUtils.convertPostResponse(postService.updatePost(postId,request)));
    }




    @PostMapping(path = "post/images", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ApiOperation(value = "포스트 이미지 업로드")
    public ApiResult<List<String>> images(
            @AuthenticationPrincipal JwtAuthentication authentication,
            @RequestPart(required = false)  MultipartFile[] images,
            MultipartHttpServletRequest request
    ) throws IOException {


        return OK(postService.uploadImage(images,request.getServletContext().getRealPath("/")));
    }


    @PostMapping(path = "post/{postId}/retweet")
    @ApiOperation(value = "리트윗")
    public ApiResult<PostResponse> retweet(
            @AuthenticationPrincipal JwtAuthentication authentication,
            @PathVariable
                    Long postId
    ) {
        return OK(dtoUtils.convertPostResponse(postService.retweet(postId,authentication.id.getValue())));
    }


    @PatchMapping(path = "user/{userId}/post/{postId}/like")
    @ApiOperation(value = "포스트 좋아요")
    public ApiResult<PostResponse> like(
            @AuthenticationPrincipal JwtAuthentication authentication,
            @PathVariable
            @ApiParam(value = "조회대상자 PK (본인 또는 친구)", example = "1")
                    Long userId,
            @PathVariable
            @ApiParam(value = "대상 포스트 PK", example = "1")
                    Long postId
    ) {
        return OK(dtoUtils.convertPostResponse(postService.like(postId, authentication.id.getValue(),userId)));
    }


    @DeleteMapping(path = "/post/{postId}")
    @ApiOperation(value = "특정 포스트 삭제")
    public ApiResult<Long> removePost(
            @AuthenticationPrincipal JwtAuthentication authentication,
            @PathVariable
            @ApiParam(value = "대상 포스트 PK", example = "1")
            Long postId
    ) {
        return OK(postService.removePost(authentication.id.getValue(), postId));
    }


    @DeleteMapping(path = "user/{userId}/post/{postId}/unlike")
    @ApiOperation(value = "포스트 좋아요 취소")
    public ApiResult<PostResponse> unlike(
            @AuthenticationPrincipal JwtAuthentication authentication,
            @PathVariable
            @ApiParam(value = "조회대상자 PK (본인 또는 친구)", example = "1")
            Long userId,
            @PathVariable
            @ApiParam(value = "대상 포스트 PK", example = "1")
            Long postId
    ) {
        return OK(dtoUtils.convertPostResponse(postService.unlike(postId, authentication.id.getValue(),userId)));
    }

}
