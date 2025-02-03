package com.astrovis.astrovisbackend.Controller;


import com.astrovis.astrovisbackend.commons.Response;
import com.astrovis.astrovisbackend.commons.ResponseConstants;
import com.astrovis.astrovisbackend.components.JwtManager;
import com.astrovis.astrovisbackend.exception.IllegalParamException;
import com.astrovis.astrovisbackend.model.User;
import com.astrovis.astrovisbackend.model.UserExample;
import com.astrovis.astrovisbackend.services.UserService;
import com.astrovis.astrovisbackend.utils.ParamsUtils;
import com.astrovis.astrovisbackend.wrapper.UserWrapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.apache.ibatis.javassist.tools.web.BadHttpRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/user")
public class UserController {


    @Autowired
    private UserService userService;
    @Autowired
    private JwtManager jwtManager;

    @Autowired
    private PasswordEncoder passwordEncoder;


    private static final byte MAX_USERNAME_LENGTH =  30;
    private static final byte MAX_PASSWORD_LENGTH = 16;





    private boolean checkLoginParams(User user){
        String username = user.getUsername();
        String password = user.getPassword();
        if (StringUtils.isBlank(username)||StringUtils.isBlank(password)){
            return false;
        }
        return true;
    }


    private Map<String,Object> loginWrongResult(){
        Map<String,Object> result = new HashMap<>();
        result.put("status",0);
        result.put("msg","Wrong username or password");
        return result;

    }

    @PostMapping("login")
    @ResponseBody
    public Response<Map<String,Object>> login(@RequestBody @Validated User user){
        Response<Map<String,Object>> response = null;

        if (!checkLoginParams(user)){
            Response.error(ResponseConstants.CODE_BAD_REQUEST,ResponseConstants.MSG_BAD_REQUEST);
        }
        if (user.getPassword().length()>MAX_PASSWORD_LENGTH || user.getUsername().length()>MAX_USERNAME_LENGTH){
            return Response.ok(loginWrongResult());
        }
        UserExample userExample = new UserExample();
        userExample.createCriteria().andUsernameEqualTo(user.getUsername());
        User loginUser = userService.selectFirstByExample(userExample);
        if (null == loginUser || !passwordEncoder.matches(user.getPassword(), loginUser.getPassword())){
            return Response.ok(loginWrongResult());
        }
        String token = jwtManager.generateToken(new UserWrapper(user));
        Map<String,Object> result = generateResultTemplate((byte) 1);
        result.put("token",token);
        return Response.ok(result);
    }

    private Map<String,Object> generateResultTemplate(byte status){
        Map<String,Object> result = new HashMap<>();
        result.put("status",status);
        return result;

    }



    @PostMapping("register")
    @ResponseBody
    @Transactional
    public Response<Map<String,Object>> register(@RequestBody @Validated User user){


        String username = user.getUsername();
        String password = user.getPassword();

        String passwordRegex = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{1,}$";
        String usernameRegex = "^[a-zA-Z0-9]{1,}$";
        if (StringUtils.isBlank(username) || StringUtils.isBlank(password)
                || username.length()>MAX_USERNAME_LENGTH
                || password.length() >MAX_PASSWORD_LENGTH
                || !username.matches(usernameRegex)
                || !password.matches(usernameRegex)){
            throw new IllegalParamException();
        }


        UserExample userExample = new UserExample();
        userExample.createCriteria().andUsernameEqualTo(username);
        if(userService.selectFirstByExample(userExample)!=null){
            Map<String,Object> result = generateResultTemplate((byte) 0);
            result.put("msg","username exists!");
            return Response.ok(result);
        }

        User registerUser = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        boolean insertResult = userService.insert(user)>0;
        Map<String,Object> result = generateResultTemplate(insertResult?(byte) 1:(byte)0);
        String fieldMsg = "Unable to create user";
        result.put("msg",insertResult?ResponseConstants.MSG_OK:fieldMsg);
        return Response.ok(result);








    }

}
