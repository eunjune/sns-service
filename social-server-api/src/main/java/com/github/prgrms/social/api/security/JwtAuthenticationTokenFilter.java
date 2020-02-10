package com.github.prgrms.social.api.security;

import com.github.prgrms.social.api.model.user.Email;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

// JWT 인증을 위한 커스텀 필터(SecurityContextPersisenceFilter의 일부 기능을 대신 수행함)
@Slf4j
public class JwtAuthenticationTokenFilter extends GenericFilterBean {

    private static final Pattern BEARER = Pattern.compile("^Bearer$", Pattern.CASE_INSENSITIVE);

    @Value("${jwt.token.header}") private String tokenHeader;

    @Autowired private JWT jwt;

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        /*
        * 인증이 필요한 요청일 때 무조건 거쳐서 토큰 정보를 확인한다.
        * (permitAll 아닌 요청)
        *
        * 그 외의 요청은 else로 간다.
        * */

        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            String authorizationToken = obtainAuthorizationToken(request);
            if (authorizationToken != null) {
                try {
                    JWT.Claims claims = verify(request, authorizationToken);
                    log.debug("Jwt parse result: {}", claims);

                    // 만료 10분 전
                    if (canRefresh(claims, 6000 * 10)) {
                        String refreshedToken = jwt.refreshToken(authorizationToken);
                        response.setHeader(tokenHeader, refreshedToken);
                    }

                    Long userKey = claims.userKey;
                    String name = claims.name;
                    Email email = claims.email;
                    List<GrantedAuthority> authorities = obtainAuthorities(claims);

                    // 요청을 처리하는 다른 클래스에서 Authentication을 쓰도록 하기 위해 스레드 로컬에 저장
                    if (nonNull(userKey) && isNotEmpty(name) && nonNull(email) && authorities.size() > 0) {
                        JwtAuthenticationToken authentication =
                                new JwtAuthenticationToken(new JwtAuthentication(userKey, name, email), null, authorities);
                        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    }
                } catch (Exception e) {
                    log.warn("Jwt processing failed: {}", e.getMessage());
                }
            }
        }
        else {
            log.debug("SecurityContextHolder not populated with security token, as it already contained: '{}'",
                    SecurityContextHolder.getContext().getAuthentication());
        }

        /*System.out.println("테스트 필터 : " + request == null);
        System.out.println("테스트 필터 : " + response == null);*/
        chain.doFilter(request, response);
    }

    private boolean canRefresh(JWT.Claims claims, long refreshRangeMillis) {
        long exp = claims.exp();
        if (exp > 0) {
            long remain = exp - System.currentTimeMillis();
            return remain < refreshRangeMillis;
        }
        return false;
    }

    private List<GrantedAuthority> obtainAuthorities(JWT.Claims claims) {
        String[] roles = claims.roles;
        return roles == null || roles.length == 0 ?
                Collections.emptyList() :
                Arrays.stream(roles).map(SimpleGrantedAuthority::new).collect(toList());
    }

    // Bearer ~~~에서 JWT 토큰 문자열만 추출.
    private String obtainAuthorizationToken(HttpServletRequest request) {
        String token = request.getHeader(tokenHeader);
        if (token != null) {
            if (log.isDebugEnabled())
                log.debug("Jwt authorization api detected: {}", token);
            try {
                token = URLDecoder.decode(token, "UTF-8");
                String[] parts = token.split(" ");
                if (parts.length == 2) {
                    String scheme = parts[0];
                    String credentials = parts[1];
                    return BEARER.matcher(scheme).matches() ? credentials : null;
                }
            } catch (UnsupportedEncodingException e) {
                log.error(e.getMessage(), e);
            }
        }

        return null;
    }

    private JWT.Claims verify(HttpServletRequest request, String token) {
        return jwt.verify(token);
    }

}