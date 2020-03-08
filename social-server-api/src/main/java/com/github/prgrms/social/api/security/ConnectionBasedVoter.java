package com.github.prgrms.social.api.security;

import com.github.prgrms.social.api.model.commons.Id;
import com.github.prgrms.social.api.model.user.User;
import com.github.prgrms.social.api.repository.user.projection.ConnectedId;
import com.github.prgrms.social.api.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.apache.commons.lang3.ClassUtils.isAssignable;

public class ConnectionBasedVoter implements AccessDecisionVoter<FilterInvocation> {

    private final RequestMatcher requiresAuthorizationRequestMatcher;

    private final Function<String, Id<User, Long>> idExtractor;

    private UserService userService;

    public ConnectionBasedVoter(RequestMatcher requiresAuthorizationRequestMatcher, Function<String, Id<User, Long>> idExtractor) {
        checkNotNull(requiresAuthorizationRequestMatcher, "requiresAuthorizationRequestMatcher must be provided.");
        checkNotNull(idExtractor, "idExtractor must be provided.");

        this.requiresAuthorizationRequestMatcher = requiresAuthorizationRequestMatcher;
        this.idExtractor = idExtractor;
    }

    @Override
    public int vote(Authentication authentication, FilterInvocation fi, Collection<ConfigAttribute> attributes) {

        HttpServletRequest request = fi.getRequest();

        // 패턴 URL이 아닌 경우
        if (!requiresAuthorizationRequestMatcher.matches(request)) {
            return ACCESS_GRANTED;
        }

        if (!isAssignable(JwtAuthenticationToken.class, authentication.getClass())) {
            return ACCESS_ABSTAIN;
        }

        String uri = request.getRequestURI();
        Id<User,Long> uriId = idExtractor.apply(uri);
        JwtAuthentication jwtAuthentication = (JwtAuthentication) authentication.getPrincipal();

        // 본인인 경우 승인
        if (jwtAuthentication.id.equals(uriId)) {
            return ACCESS_GRANTED;
        }

        // 친구관계면 승인
        List<ConnectedId> connectedIds = userService.findConnectedIds(jwtAuthentication.id.getValue());
        for(int i=0; i<connectedIds.size(); ++i) {
            if(connectedIds.get(i).getTargetUserId().equals(uriId.getValue())) {
                return ACCESS_GRANTED;
            }
        }

        return ACCESS_DENIED;
    }

    @Override
    public boolean supports(ConfigAttribute attribute) {
        return true;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return isAssignable(FilterInvocation.class, clazz);
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

}
