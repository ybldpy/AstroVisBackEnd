package com.astrovis.astrovisbackend.Controller;


import com.astrovis.astrovisbackend.commons.Response;
import com.astrovis.astrovisbackend.model.User;
import com.astrovis.astrovisbackend.services.UserService;
import com.astrovis.astrovisbackend.utils.ParamsUtils;
import com.astrovis.astrovisbackend.utils.ResponseUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/user")
public class UserController {


    @Autowired
    private UserService userService;


    private static final String LOG_IN_PARAMS_USERNAME = "username";
    private static final String LOG_IN_PARAMS_PASSWORD = "password";
    private static final String LOG_IN_PARAMS_REMEMBER = "rememberMe";





    private Response<Map<String,Object>> checkLoginParams(User user){
        String username = user.getUsername();
        String password = user.getPassword();
        if (StringUtils.isBlank(username)||StringUtils.isBlank(password)){
            return ResponseUtils.createIllegalParamsResponse();
        }
        return null;
    }

    @PostMapping("login")
    @ResponseBody
    public Response<Map<String,Object>> login(@RequestBody @Validated User user){
        Response<Map<String,Object>> response = null;
        response = this.checkLoginParams(user);
        if (response!=null){return response;}
        Map<String,Object> result = new HashMap<>();
        return new Response<>(200,null,result);

    }

}
