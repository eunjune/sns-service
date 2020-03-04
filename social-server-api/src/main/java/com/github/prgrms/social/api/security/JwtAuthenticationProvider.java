package com.github.prgrms.social.api.security;

import com.github.prgrms.social.api.error.NotFoundException;
import com.github.prgrms.social.api.model.user.Email;
import com.github.prgrms.social.api.model.user.Role;
import com.github.prgrms.social.api.model.user.User;
import com.github.prgrms.social.api.service.user.UserService;
import org.springframework.dao.DataAccessException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.apache.commons.lang3.ClassUtils.isAssignable;
import static org.springframework.security.core.authority.AuthorityUtils.createAuthorityList;

public class JwtAuthenticationProvider implements AuthenticationProvider {

    private final JWT jwt;

    private final UserService userService;

    public JwtAuthenticationProvider(JWT jwt, UserService userService) {
        this.jwt = jwt;
        this.userService = userService;
    }

    //인증 안 된 Authentication을 인증된 Authentication으로 변경.
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        JwtAuthenticationToken authenticationToken = (JwtAuthenticationToken) authentication;
        return processUserAuthentication(authenticationToken.authenticationRequest());
    }

    // 로그인 인증 확인 후 JWT 토큰 생성
    private Authentication processUserAuthentication(AuthenticationRequest request) {
        try {
            User user = userService.login(new Email(request.getAddress()), request.getPassword());
            JwtAuthenticationToken authenticated =
                    new JwtAuthenticationToken(user.getId(), null, createAuthorityList(Role.USER.getValue()));
            String apiToken = user.newApiToken(jwt, new String[]{Role.USER.getValue()});
            authenticated.setDetails(new AuthenticationResult(apiToken, user));
            return authenticated;
        } catch (NotFoundException e) {
            throw new UsernameNotFoundException(e.getMessage());
        } catch (IllegalArgumentException e) {
            throw new BadCredentialsException(e.getMessage());
        } catch (DataAccessException e) {
            throw new AuthenticationServiceException(e.getMessage(), e);
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return isAssignable(JwtAuthenticationToken.class, authentication);
    }

}
