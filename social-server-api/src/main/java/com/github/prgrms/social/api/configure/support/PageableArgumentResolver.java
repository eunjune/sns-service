package com.github.prgrms.social.api.configure.support;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import static org.apache.commons.lang3.math.NumberUtils.toInt;
import static org.apache.commons.lang3.math.NumberUtils.toLong;
import static org.springframework.util.StringUtils.hasText;

public class PageableArgumentResolver implements HandlerMethodArgumentResolver {

    private static final int MAX_LIMIT = 5;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return Pageable.class.isAssignableFrom(parameter.getParameterType());
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

        String offsetString = webRequest.getParameter("offset");
        String limitString = webRequest.getParameter("limit");

        boolean validString = hasText(offsetString) && hasText(limitString);

        if(!validString)
            return new PageableImpl();

        // offset 0~MAX
        // limit 1~5
        long offset = toLong(offsetString, 0);
        int limit = toInt(limitString, MAX_LIMIT);

        if(limit < 1 || limit > MAX_LIMIT)
            limit = MAX_LIMIT;

        if(offset < 0 || offset > Integer.MAX_VALUE)
            offset = 0;

        return new PageableImpl(offset, limit);
    }

}