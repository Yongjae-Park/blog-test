package com.example.blogtest.application.common.config;

import com.example.blogtest.application.common.exception.blog.PageSizeValueOutOfBoundsException;
import com.example.blogtest.application.common.exception.blog.PageValueOutOfBoundsException;
import org.springframework.core.MethodParameter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;

public class KakaoBlogSearchArgumentResolver extends PageableHandlerMethodArgumentResolver {

    @Override
    public Pageable resolveArgument(final MethodParameter methodParameter, final ModelAndViewContainer mavContainer,
                                    final NativeWebRequest webRequest, final WebDataBinderFactory binderFactory) {
        String pageSizeValue = webRequest.getParameter(getParameterNameToUse(getSizeParameterName(), methodParameter));
        validatePageSizeValue(pageSizeValue);
        String pageValue = webRequest.getParameter(getParameterNameToUse(getPageParameterName(), methodParameter));
        validatePageValue(pageValue);

        setFallbackPageable(PageRequest.of(1, 10));

        return super.resolveArgument(methodParameter, mavContainer, webRequest, binderFactory);
    }

    private void validatePageSizeValue(String pageSize) {
        if ((pageSize != null && !pageSize.equals("")) && (Integer.parseInt(pageSize) > 50 || Integer.parseInt(pageSize) < 1)) {
            throw new PageSizeValueOutOfBoundsException();
        }
    }
    private void validatePageValue(String pageValue) {
        if ((pageValue != null && !pageValue.equals("")) && (Integer.parseInt(pageValue) > 50 || Integer.parseInt(pageValue) < 1)) {
            throw new PageValueOutOfBoundsException();
        }
    }
}
