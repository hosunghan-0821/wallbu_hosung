package kr.co.hanhosung.wallbu.global.config;

import kr.co.hanhosung.wallbu.global.filter.JwtAuthFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.FilterRegistration;

@Slf4j
@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean<JwtAuthFilter> jwtAuthFilter(){

        FilterRegistrationBean<JwtAuthFilter> registrationBean = new FilterRegistrationBean<JwtAuthFilter>();
        registrationBean.setFilter(new JwtAuthFilter());
        registrationBean.addUrlPatterns("/api/user/*");
        return registrationBean;
    }
}
