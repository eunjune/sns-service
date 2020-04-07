package com.github.prgrms.social.api.configure;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.github.prgrms.social.api.model.api.request.user.ProfileRequest;
import com.github.prgrms.social.api.model.api.response.post.PostResponse;
import com.github.prgrms.social.api.model.api.response.user.MeResponse;
import com.github.prgrms.social.api.model.api.response.user.UserResponse;
import com.github.prgrms.social.api.model.post.Comment;
import com.github.prgrms.social.api.model.post.Image;
import com.github.prgrms.social.api.model.post.LikeInfo;
import com.github.prgrms.social.api.model.post.Post;
import com.github.prgrms.social.api.model.user.Email;
import com.github.prgrms.social.api.model.user.User;
import com.github.prgrms.social.api.security.JWT;
import com.github.prgrms.social.api.util.MessageUtils;
import com.zaxxer.hikari.HikariDataSource;
import net.sf.log4jdbc.Log4jdbcProxyDataSource;
import org.modelmapper.Condition;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.NameTokenizers;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import javax.sql.DataSource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;

@Configuration
public class ServiceConfigure {

    // 테스트시 사용할 데이터베이스
    @Bean
    @Profile("test")
    public DataSource testDataSource() {
        DataSourceBuilder factory = DataSourceBuilder
                .create()
                .driverClassName("org.h2.Driver")
                .url("jdbc:h2:mem:test_social_server_api;MODE=MYSQL;DB_CLOSE_DELAY=-1")
                .username("sa");
        HikariDataSource dataSource = (HikariDataSource) factory.build();
        dataSource.setPoolName("TEST_H2_DB");
        dataSource.setMinimumIdle(1);
        dataSource.setMaximumPoolSize(1);
        return new Log4jdbcProxyDataSource(dataSource);
    }

    // messageSource를 더 편리하게 사용하기 위해
    @Bean
    public MessageSourceAccessor messageSourceAccessor(MessageSource messageSource) {
        return new MessageSourceAccessor(messageSource);
    }

    @Bean
    public MessageUtils messageUtils() {
        return MessageUtils.getInstance();
    }

    @Value("${jwt.token.issuer}") String issuer;
    @Value("${jwt.token.clientSecret}") String clientSecret;
    @Value("${jwt.token.expirySeconds}") int expirySeconds;

    @Bean
    public JWT jwt() {
        return new JWT(issuer, clientSecret, expirySeconds);
    }

    // ObjectMapper를 커스터마이징. (여기서는 시간 관련 클래스들과 JSON 간의 변환을 위해)
    @Bean
    public Jackson2ObjectMapperBuilder configureObjectMapper() {
        // Java time module
        JavaTimeModule jtm = new JavaTimeModule();
        jtm.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ISO_DATE_TIME));
        Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder() {
            @Override
            public void configure(ObjectMapper objectMapper) {
                super.configure(objectMapper);
                // 어떤 객체 맴버를 JSON으로 변환할 것인지 설정. (어떤 객체 맴버, 접근 제어자)
                objectMapper.setVisibility(PropertyAccessor.GETTER, JsonAutoDetect.Visibility.NONE);
                objectMapper.setVisibility(PropertyAccessor.IS_GETTER, JsonAutoDetect.Visibility.NONE);
                objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
            }
        };
        // null인 맴버 값은 serialize 하지 않음.
        builder.serializationInclusion(JsonInclude.Include.NON_NULL);
        // 이 설정을 하지 않으면 JSON에 쓸데없는 정보까지 포함함.
        builder.featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        builder.modulesToInstall(jtm);
        return builder;
    }

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        // TODO : 더 효율적인 방법
        Converter<Set<User>, Integer> toUserCount = context -> context.getSource().size();
        Converter<Set<Post>, Integer> toPostCount = context -> context.getSource().size();
        Converter<Set<LikeInfo>, Integer> toLikeCount = context -> context.getSource().size();
        Converter<Set<Comment>, Integer> toCommentCount = context -> context.getSource().size();
        Converter<Set<Image>, Set<String>> setImages = context -> {
            Set<Image> source = context.getSource();
            Set<String> definition = new HashSet<>();

            for(Image image : source) {
                definition.add(image.getPath());
            }

            return definition;
        };

        Converter<Email, String> toEmail = context -> context.getSource().getAddress();

        Condition<String,String> notNullAndEmptyString = ctx -> ctx.getSource() != null && !ctx.getSource().isEmpty();

        modelMapper.getConfiguration()
                .setDestinationNameTokenizer(NameTokenizers.UNDERSCORE)
                .setSourceNameTokenizer(NameTokenizers.UNDERSCORE);

        modelMapper
                .typeMap(ProfileRequest.class, User.class)
                .addMappings(mapper -> mapper.when(notNullAndEmptyString).map(ProfileRequest::getName, User::setName))
                .addMappings(mapper -> mapper.when(notNullAndEmptyString).map(ProfileRequest::getPassword, User::setPassword));

        modelMapper
                .typeMap(User.class, MeResponse.class)
                .addMappings(mapper -> mapper.using(toEmail).map(User::getEmail, MeResponse::setEmail))
                .addMappings(mapper -> mapper.using(toUserCount).map(User::getFollowings, MeResponse::setFollowingCount))
                .addMappings(mapper -> mapper.using(toUserCount).map(User::getFollowers, MeResponse::setFollowerCount));

        modelMapper
                .typeMap(User.class, UserResponse.class)
                .addMappings(mapper -> mapper.using(toUserCount).map(User::getFollowings, UserResponse::setFollowingCount))
                .addMappings(mapper -> mapper.using(toUserCount).map(User::getFollowers, UserResponse::setFollowerCount));


        modelMapper
                .typeMap(Post.class, PostResponse.class)
                .addMappings(mapper -> mapper.using(toLikeCount).map(Post::getLikeInfos, PostResponse::setLikeCount))
                .addMappings(mapper -> mapper.using(toCommentCount).map(Post::getComments, PostResponse::setCommentCount))
                .addMappings(mapper -> mapper.skip(PostResponse::setUser))
                .addMappings(mapper -> mapper.skip(PostResponse::setRetweetPost))
                .addMappings(mapping -> mapping.using(setImages).map(Post::getImages, PostResponse::setImages));


        return modelMapper;
    }
}
