package com.github.prgrms.social.api.security.voter;

import com.github.prgrms.social.api.model.commons.Id;
import com.github.prgrms.social.api.model.user.User;
import com.github.prgrms.social.api.repository.user.UserRepository;
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
import java.util.List;
import java.util.function.Function;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.apache.commons.lang3.ClassUtils.isAssignable;

public class ConnectionBasedVoter implements AccessDecisionVoter<FilterInvocation> {

    private final RequestMatcher requiresAuthorizationRequestMatcher;

    private final Function<String, Id<User, Long>> idExtractor;

    @Autowired
    private UserRepository userRepository;

    public ConnectionBasedVoter(RequestMatcher requiresAuthorizationRequestMatcher, Function<String, Id<User, Long>> idExtractor) {
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

        /*Boolean isEmailCertification = userRepository.findEmailCertificationById(jwtAuthentication.id.getValue()).getIsEmailCertification();
        if(!isEmailCertification) {
            return ACCESS_DENIED;
        }*/

        // 패턴 URL이 아닌 경우
        if (!requiresAuthorizationRequestMatcher.matches(request)) {
            return ACCESS_GRANTED;
        }

        // 본인인 경우 승인
        if (jwtAuthentication.id.equals(uriId) || uriId.getValue() == 0L) {
            return ACCESS_GRANTED;
        }

        // 공개 계정인 경우 승인
        if(!userRepository.findPrivateById(uriId.getValue()).getIsPrivate()) {
            return ACCESS_GRANTED;
        }

        // 비공개 계정인 경우 팔로워만 승인
        List<User> followers = userRepository.findFollowersAllById(uriId.getValue()).getFollowers();
        for(User follower : followers) {
            if(follower.getId().equals(jwtAuthentication.id.getValue())) {
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
    public void setUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

}
