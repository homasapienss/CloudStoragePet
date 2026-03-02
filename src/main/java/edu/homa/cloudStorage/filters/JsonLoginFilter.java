package edu.homa.cloudStorage.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.homa.cloudStorage.dto.auth.req.SignInRequest;
import edu.homa.cloudStorage.dto.auth.resp.SignInResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class JsonLoginFilter extends UsernamePasswordAuthenticationFilter {
    private final ObjectMapper objectMapper = new ObjectMapper();
    AuthenticationSuccessHandler successHandler = (request, response, authentication) -> {
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json");
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        objectMapper.writeValue(response.getWriter(), new SignInResponse(authentication.getName()));
    };
    AuthenticationFailureHandler failureHandler = (request, response, exception) -> {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.getWriter().write("{\"error\":\"Bad credentials\"}");
    };

    public JsonLoginFilter(AuthenticationManager authenticationManager) {
        setAuthenticationManager(authenticationManager);
        setFilterProcessesUrl("/api/auth/sign-in");
        setAuthenticationSuccessHandler(successHandler);
        setAuthenticationFailureHandler(failureHandler);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {
        String contentType = request.getContentType();
        if (contentType == null || !contentType.startsWith("application/json")) {
            throw new AuthenticationServiceException("Unsupported content type: " + request.getContentType());
        }
        try (InputStream is = request.getInputStream()){
            SignInRequest signInRequest = objectMapper.readValue(is, SignInRequest.class);
            UsernamePasswordAuthenticationToken authReq = new UsernamePasswordAuthenticationToken(signInRequest.username(), signInRequest.password());
            setDetails(request, authReq);
            return this.getAuthenticationManager().authenticate(authReq);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
