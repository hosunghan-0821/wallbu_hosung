package kr.co.hanhosung.wallbu.global.config;

import kr.co.hanhosung.wallbu.global.annotation.LoginUserArgumentResolver;
import kr.co.hanhosung.wallbu.global.util.token.ITokenManager;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final LoginUserArgumentResolver loginUserArgumentResolver;

    public WebMvcConfig(LoginUserArgumentResolver loginUserArgumentResolver) {
        this.loginUserArgumentResolver = loginUserArgumentResolver;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(loginUserArgumentResolver);
    }
}
