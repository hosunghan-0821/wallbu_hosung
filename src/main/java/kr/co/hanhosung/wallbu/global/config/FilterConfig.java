package kr.co.hanhosung.wallbu.global.config;

import kr.co.hanhosung.wallbu.global.filter.JwtAuthFilter;
import kr.co.hanhosung.wallbu.global.util.token.ITokenManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.FilterRegistration;

@Slf4j
@Configuration
public class FilterConfig {

    private final ITokenManager iTokenManager;

    public FilterConfig(ITokenManager iTokenManager) {
        this.iTokenManager = iTokenManager;
    }

    @Bean
    public FilterRegistrationBean<JwtAuthFilter> jwtAuthFilter() {

        FilterRegistrationBean<JwtAuthFilter> registrationBean = new FilterRegistrationBean<JwtAuthFilter>();
        registrationBean.setFilter(new JwtAuthFilter(iTokenManager));
        registrationBean.addUrlPatterns("/api/lectures/*","/api/user/*");
        return registrationBean;
    }
}
