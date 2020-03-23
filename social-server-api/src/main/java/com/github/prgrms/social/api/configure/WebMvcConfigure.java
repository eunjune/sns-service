package com.github.prgrms.social.api.configure;

import com.github.prgrms.social.api.configure.support.PageableArgumentResolver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.PathResourceResolver;

import java.util.List;

@Configuration
public class WebMvcConfigure implements WebMvcConfigurer {

    @Value("${jwt.token.header}") private String tokenHeader;

    private String baseApiPath = "/api";

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry
            .addResourceHandler("/**/*.css", "/**/*.html", "/**/*.js", "/**/*.jsx", "/**/*.png", "/**/*.ttf", "/**/*.woff", "/**/*.woff2", "/manifest.json")
            .setCachePeriod(0)
            .addResourceLocations("classpath:/static/");

        registry
            .addResourceHandler("/image/**").addResourceLocations("file:/Users/iws16/AppData/Local/Temp/uploads/").setCachePeriod(0);


        registry
                .addResourceHandler("/image/profile/**").addResourceLocations("file:/Users/iws16/AppData/Local/Temp/uploads/profile/").setCachePeriod(0);

        registry
            .addResourceHandler("/", "/**")
            .setCachePeriod(0)
            .addResourceLocations("classpath:/static/index.html")
            .resourceChain(true)
            .addResolver(new PathResourceResolver() {
                @Override
                protected Resource getResource(String resourcePath, Resource location) {
                if (resourcePath.startsWith(baseApiPath) || resourcePath.startsWith(baseApiPath.substring(1))) {
                    return null;
                }
                return location.exists() && location.isReadable() ? location : null;
                }
            });
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(new PageableArgumentResolver());
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:3060")
                .allowedMethods("GET","POST","PUT","PATCH","DELETE","OPTIONS");

    }

}
