package com.aimed.linkedin.posts_service.auth;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class FeignClientInterceptor implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate requestTemplate) {
        Long userId = UserContextHolder.getCurrentUserId();
        if (!Objects.isNull(userId)) {
            requestTemplate.header("X-User-Id", userId.toString());
        }
    }
}
