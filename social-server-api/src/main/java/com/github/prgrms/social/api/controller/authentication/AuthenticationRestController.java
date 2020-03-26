package com.github.prgrms.social.api.controller.authentication;

import com.github.prgrms.social.api.error.UnauthorizedException;
import com.github.prgrms.social.api.model.api.response.ApiResult;
import com.github.prgrms.social.api.model.user.User;
import com.github.prgrms.social.api.security.AuthenticationRequest;
import com.github.prgrms.social.api.security.AuthenticationResult;
import com.github.prgrms.social.api.security.JwtAuthenticationToken;
import com.github.prgrms.social.api.service.user.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import static com.github.prgrms.social.api.model.api.response.ApiResult.OK;

@RestController
@RequestMapping("api/auth")
@Api(tags = "인증 APIs")
@RequiredArgsConstructor
public class AuthenticationRestController {

    private final AuthenticationManager authenticationManager;

    private final UserService userService;

    @PostMapping
    @ApiOperation(value = "사용자 로그인 (API 토큰 필요없음)")
    public ApiResult<AuthenticationResult> authentication(@RequestBody AuthenticationRequest authRequest) throws UnauthorizedException {
        try {
            JwtAuthenticationToken authToken = new JwtAuthenticationToken(authRequest.getAddress(), authRequest.getPassword());
            Authentication authentication = authenticationManager.authenticate(authToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            return OK((AuthenticationResult) authentication.getDetails());
        } catch (AuthenticationException e) {
            throw new UnauthorizedException(e.getMessage());
        }
    }

    @GetMapping("check-email-token")
    public ApiResult<User> checkEmailToken(String token, String email) {
        return OK(userService.certificateEmail(token,email));
    }

}
