package kr.co.hanhosung.wallbu.global.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.hanhosung.wallbu.global.error.dto.ErrorCode;
import kr.co.hanhosung.wallbu.global.error.dto.ErrorResponseDto;
import kr.co.hanhosung.wallbu.global.util.token.ITokenManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class JwtAuthFilter implements Filter {

    private final ITokenManager iTokenManager;

    public JwtAuthFilter(ITokenManager iTokenManager) {
        this.iTokenManager = iTokenManager;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
        log.info("JwtAuthFilter init()");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String accessToken = httpRequest.getHeader(HttpHeaders.AUTHORIZATION);

        if (accessToken == null || accessToken.isEmpty()) {
            httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            httpResponse.setContentType("application/json");
            httpResponse.setCharacterEncoding("UTF-8");

            ObjectMapper objectMapper = new ObjectMapper();
            ErrorResponseDto errorResponseDto = ErrorResponseDto.builder()
                    .errorCode(ErrorCode.AUTHORIZATION_EXCEPTION.getErrorCode())
                    .message(ErrorCode.AUTHORIZATION_EXCEPTION.getDefaultMessage())
                    .build();

            httpResponse.getWriter().write(objectMapper.writeValueAsString(errorResponseDto));
            return;
        }

        iTokenManager.verifyToken(accessToken);

        filterChain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
