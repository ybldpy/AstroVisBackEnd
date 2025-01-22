package com.astrovis.astrovisbackend.auth;

import com.astrovis.astrovisbackend.commons.Response;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class AuthEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        Response returnReponse = new Response(403,"Forbidden",null);

        PrintWriter output = response.getWriter();
        output.write(new ObjectMapper().writeValueAsString(returnReponse));
        output.flush();
        output.close();

    }
}
