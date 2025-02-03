package com.astrovis.astrovisbackend.Controller;


import com.astrovis.astrovisbackend.components.JwtManager;
import com.astrovis.astrovisbackend.model.User;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("apiTest")
public class TestController {

    @Autowired
    JwtManager jwtManager;

    @GetMapping("jwt")
    @ResponseBody
    public String testSecurity(HttpServletRequest httpServletRequest){
        String auth = httpServletRequest.getHeader("Authorization");
        Claims claims = jwtManager.getClaimsFromToken(auth);
        return claims.getSubject();
    }


}
