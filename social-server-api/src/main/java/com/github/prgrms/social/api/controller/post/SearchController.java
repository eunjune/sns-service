package com.github.prgrms.social.api.controller.post;

import com.github.prgrms.social.api.service.post.PostService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api")
@Api(tags = "검색 APIs")
public class SearchController {

    private final PostService postService;

    @GetMapping("/search/{keyword}")
    public String search(
            @ApiParam(value = "검색 키워드") @PathVariable String keyword

    ) {



        return "";
    }
}
