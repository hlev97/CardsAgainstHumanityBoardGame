package hu.bme.cah.api.cardsagainsthumanityapi.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * {@link LogoutSuccessHandler}: giving feedback on successful log out
 */
@Slf4j
public class LogoutSuccessHandler extends SimpleUrlLogoutSuccessHandler {
    private ObjectMapper objectMapper = Jackson2ObjectMapperBuilder.json().build();

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.trace("onLogoutSuccess method is accessed");
        String json = objectMapper.writeValueAsString("{\"message\":\"Successfully logged out\"}");
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(json);
    }
}
