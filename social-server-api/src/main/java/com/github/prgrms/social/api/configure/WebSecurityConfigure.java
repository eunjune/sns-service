package com.github.prgrms.social.api.configure;

import com.github.prgrms.social.api.model.commons.Id;
import com.github.prgrms.social.api.model.post.Post;
import com.github.prgrms.social.api.model.user.User;
import com.github.prgrms.social.api.security.*;
import com.github.prgrms.social.api.security.voter.ConnectionBasedVoter;
import com.github.prgrms.social.api.security.voter.NotificationVoter;
import com.github.prgrms.social.api.security.voter.PostEditVoter;
import com.github.prgrms.social.api.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.vote.UnanimousBased;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.expression.WebExpressionVoter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.RegexRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.apache.commons.lang3.math.NumberUtils.toLong;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfigure extends WebSecurityConfigurerAdapter {

    private final JwtAccessDeniedHandler accessDeniedHandler;

    private final EntryPointUnauthorizedHandler unauthorizedHandler;


    @Autowired
    public void configureAuthentication(AuthenticationManagerBuilder builder, JwtAuthenticationProvider authenticationProvider) {
        builder.authenticationProvider(authenticationProvider);
    }

    @Bean
    public JwtAuthenticationProvider jwtAuthenticationProvider(JWT jwt,UserService userService) {
        return new JwtAuthenticationProvider(jwt, userService);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter() {
        return new JwtAuthenticationTokenFilter();
    }

    @Bean
    public ConnectionBasedVoter connectionBasedVoter() {
        Pattern pattern = Pattern.compile( "/api/user/(\\d+)/.*");
        RequestMatcher requestMatcher = new RegexRequestMatcher(pattern.pattern(),null);

        return new ConnectionBasedVoter(requestMatcher, (url) -> {
            Matcher matcher = pattern.matcher(url);
            return matcher.find() ? toLong(matcher.group(1), -1) : -1;
        });
    }

    @Bean
    public PostEditVoter postEditVoter() {
        Pattern pattern = Pattern.compile( "/api/post/(\\d+)");
        RequestMatcher requestMatcher = new RegexRequestMatcher(pattern.pattern(),null);

        return new PostEditVoter(requestMatcher, (url) -> {
            Matcher matcher = pattern.matcher(url);
            return matcher.find() ? toLong(matcher.group(1), -1) : -1;
        });
    }

    @Bean
    public NotificationVoter notificationVoter() {
        Pattern pattern = Pattern.compile( "/api/user/notification/(\\d+)");
        RequestMatcher requestMatcher = new RegexRequestMatcher(pattern.pattern(),null);

        return new NotificationVoter(requestMatcher, (url) -> {
            Matcher matcher = pattern.matcher(url);
            return matcher.find() ? toLong(matcher.group(1), -1) : -1;
        });

    }

    @Bean
    public AccessDecisionManager accessDecisionManager() {
        List<AccessDecisionVoter<?>> decisionVoters = new ArrayList<>();
        decisionVoters.add(new WebExpressionVoter());
        decisionVoters.add(connectionBasedVoter());
        decisionVoters.add(postEditVoter());
        decisionVoters.add(notificationVoter());
        // 모든 voter가 승인해야 해야한다.
        return new UnanimousBased(decisionVoters);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .cors()
                .and()
            .csrf()
                .disable()
            .headers()
                .disable()
            .exceptionHandling()
                .accessDeniedHandler(accessDeniedHandler)
                .authenticationEntryPoint(unauthorizedHandler)
                .and()
            .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
            .authorizeRequests()
                .antMatchers("/profile").permitAll()
                .antMatchers("/api/auth/**").permitAll()
                .antMatchers("/api/user/exists/**").permitAll()
                .antMatchers("/api/user/join").permitAll()
                .antMatchers("/api/check-email-token").permitAll()
                .antMatchers("/api/user/post/list").permitAll()
                .antMatchers("/api/post/{tag}/list").permitAll()
                .antMatchers("/api/post/{postId}/comment/list").permitAll()
                .antMatchers("/api/user/{userId}/post/{postId}").permitAll()
                .antMatchers("/api/search/**").permitAll()
                .antMatchers("/api/_hcheck").permitAll()
                .antMatchers("/api/**").hasRole("USER")
                .accessDecisionManager(accessDecisionManager())
                .anyRequest().permitAll()
                .and()
            .formLogin()
                .disable();
        http
            .addFilterBefore(jwtAuthenticationTokenFilter(), UsernamePasswordAuthenticationFilter.class);

    }


    @Override
    public void configure(WebSecurity web) {
        web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations())
                        .regexMatchers("/swagger-resources","/h2/*","/templates/*");
    }

}
