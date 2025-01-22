package com.astrovis.astrovisbackend.services;


import com.astrovis.astrovisbackend.commons.Response;
import com.astrovis.astrovisbackend.mappers.UserMapper;
import com.astrovis.astrovisbackend.model.User;
import com.astrovis.astrovisbackend.model.UserExample;
import com.astrovis.astrovisbackend.wrapper.UserWrapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;


@Service("userService")
public class UserService extends BaseService<UserMapper,User,UserExample> implements UserDetailsService{

    public static final String NOT_EXIST = "user not exist";
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserExample userExample = new UserExample();
        userExample.createCriteria().andUsernameEqualTo(username);
        User user = selectFirstByExample(userExample);
        if (user == null){
            throw new UsernameNotFoundException(NOT_EXIST);
        }
        return new UserWrapper(user);
    }







}
