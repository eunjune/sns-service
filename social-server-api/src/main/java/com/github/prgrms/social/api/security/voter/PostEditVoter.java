package com.github.prgrms.social.api.security.voter;

import com.github.prgrms.social.api.model.commons.Id;
import com.github.prgrms.social.api.model.user.User;
import com.github.prgrms.social.api.repository.post.JpaPostRepository;
import com.github.prgrms.social.api.security.JwtAuthentication;
import com.github.prgrms.social.api.security.JwtAuthenticationToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.function.Function;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.apache.commons.lang3.ClassUtils.isAssignable;

public class PostEditVoter implements AccessDecisionVoter<FilterInvocation> {

    private JpaPostRepository postRepository;

    private final RequestMatcher requiresAuthorizationRequestMatcher;

    private final Function<String, Id<User, Long>> idExtractor;

    public PostEditVoter(RequestMatcher requiresAuthorizationRequestMatcher, Function<String, Id<User, Long>> idExtractor) {
        checkNotNull(requiresAuthorizationRequestMatcher, "requiresAuthorizationRequestMatcher must be provided.");
        checkNotNull(idExtractor, "idExtractor must be provided.");

        this.requiresAuthorizationRequestMatcher = requiresAuthorizationRequestMatcher;
        this.idExtractor = idExtractor;
    }

    @Override
    public int vote(Authentication authentication, FilterInvocation fi, Collection<ConfigAttribute> attributes) {

        HttpServletRequest request = fi.getRequest();
        String uri = request.getRequestURI();
        Id<User,Long> uriId = idExtractor.apply(uri);

        if (!isAssignable(JwtAuthenticationToken.class, authentication.getClass())) {
            return ACCESS_ABSTAIN;
        }

        JwtAuthentication jwtAuthentication = (JwtAuthentication) authentication.getPrincipal();

        if (!requiresAuthorizationRequestMatcher.matches(request)) {
            return ACCESS_GRANTED;
        }

        if(postRepository.findUserById(uriId.getValue()).getUser().getId().equals(jwtAuthentication.id.getValue())) {
            return ACCESS_GRANTED;
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
    public void setUserService(JpaPostRepository postRepository) {
        this.postRepository = postRepository;
    }
}
