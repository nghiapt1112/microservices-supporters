package com.nghiatut.mss.support.edge.security;

import com.nghia.libraries.commons.mss.infrustructure.exception.DomainException;
import com.nghia.libraries.commons.mss.utils.JsonUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint, Serializable {

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException e) throws IOException {
        // Error Exception info
        //        CustomError error = new CustomError(HttpStatus.FORBIDDEN, error, description);

        try {
            String json = JsonUtils.toJson(new AuthenTicationException(HttpStatus.FORBIDDEN.toString()));
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding(StandardCharsets.UTF_8.toString());
            response.getWriter().write(json);
        } catch (Exception e1) {
            e1.printStackTrace();
        }

    }

}

class AuthenTicationException extends DomainException {
    public AuthenTicationException(String message) {
        super(message);
    }
}

