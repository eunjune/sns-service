package com.github.prgrms.social.api.controller.post;

import com.github.prgrms.social.api.model.api.response.ApiResult;
import com.github.prgrms.social.api.model.api.response.post.PostResponse;
import com.github.prgrms.social.api.repository.post.PostRepository;
import com.github.prgrms.social.api.util.DtoUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;
import java.util.stream.Collectors;

import static com.github.prgrms.social.api.model.api.response.ApiResult.OK;

@RestController
@RequestMapping("api")
@RequiredArgsConstructor
@Api(tags = "검색 APIs")
public class SearchController {

    private final PostRepository postRepository;

    private final DtoUtils dtoUtils;

    @GetMapping("/search/{keyword}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "lastId", dataType = "integer", paramType = "query", defaultValue = "0", value = "마지막 포스트 PK(0이면 처음조회)"),
            @ApiImplicitParam(name = "size", dataType = "integer", paramType = "query", defaultValue = "4", value = "최대 조회 갯수")
    })
    public ApiResult<List<PostResponse>> search(
            @ApiParam(value = "검색 키워드", example = "1", required = true) @PathVariable String keyword,
            @RequestParam Long lastId,
            @ApiIgnore @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable

    ) {

        if(lastId == 0L) {
            lastId = postRepository.findFirstByOrderByIdDesc()
                                .map(postProjection -> postProjection.getId() + 1L)
                                .orElse(1L);
        }

        return OK(postRepository.findSearchByContentContainingAndIdLessThanAndUser_IsPrivateFalse(keyword,lastId,pageable)
                                .stream()
                                .map(dtoUtils::convertPostResponse)
                                .collect(Collectors.toList()));
    }
}
