package hu.bme.cah.api.cardsagainsthumanityapi.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * {@link RestAuthenticationEntryPoint}
 */
@Component
@Slf4j
public class RestAuthenticationEntryPoint extends BasicAuthenticationEntryPoint {
    /**
     * {@link ObjectMapper} to create json object
     */
    private final ObjectMapper objectMapper = Jackson2ObjectMapperBuilder.json().build();

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        log.trace("commence method is accessed");
        String authHeader = String.format("Auth realm=\"%s\"", getRealmName());
        response.addHeader("WWW-Authenticate", authHeader);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        String json = objectMapper.writeValueAsString("{\"error\":\"unauthorized\"}");
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(json);
    }

    @Override
    public void afterPropertiesSet() {
        log.trace("afterPropertiesSet method is accessed");
        setRealmName("com.example");
        super.afterPropertiesSet();
    }
}
