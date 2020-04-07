package com.github.prgrms.social.api.controller.authentication;

import com.github.prgrms.social.api.error.UnauthorizedException;
import com.github.prgrms.social.api.model.api.response.ApiResult;
import com.github.prgrms.social.api.model.api.response.user.AuthenticationResponse;
import com.github.prgrms.social.api.security.AuthenticationRequest;
import com.github.prgrms.social.api.security.AuthenticationResult;
import com.github.prgrms.social.api.security.JwtAuthenticationToken;
import com.github.prgrms.social.api.service.user.EmailService;
import com.github.prgrms.social.api.util.DtoUtils;
import io.swagger.annotations.*;
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

    private final DtoUtils dtoUtils;

    private final EmailService emailService;

    @PostMapping
    @ApiOperation(value = "사용자 로그인 (API 토큰 필요없음)")
    public ApiResult<AuthenticationResponse> authentication(@RequestBody AuthenticationRequest authRequest) throws UnauthorizedException {
        try {
            JwtAuthenticationToken authToken = new JwtAuthenticationToken(authRequest.getAddress(), authRequest.getPassword());
            Authentication authentication = authenticationManager.authenticate(authToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            AuthenticationResult authenticationResult = (AuthenticationResult) authentication.getDetails();
            return OK(dtoUtils.convertAuthenticationResponse(authenticationResult));
        } catch (AuthenticationException e) {
            throw new UnauthorizedException(e.getMessage());
        }
    }

    @GetMapping("{address}")
    @ApiOperation(value = "사용자 이메일 로그인 (API 토큰 필요없음)")
    public ApiResult<AuthenticationResponse> emailAuthentication(
            @ApiParam(value = "이메일", example = "test00@gmail.com", required = true) @PathVariable String address
    ) throws UnauthorizedException {
        try {
            JwtAuthenticationToken authToken = new JwtAuthenticationToken(address);
            Authentication authentication = authenticationManager.authenticate(authToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            AuthenticationResult authenticationResult = (AuthenticationResult) authentication.getDetails();

            emailService.sendEmailLoginLinkMessage(authenticationResult.getUser(), authenticationResult.getToken());


            return OK(dtoUtils.convertAuthenticationResponse(authenticationResult));
        } catch (AuthenticationException e) {
            throw new UnauthorizedException(e.getMessage());
        }
    }
}
