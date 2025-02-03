package com.astrovis.astrovisbackend.services;


import com.astrovis.astrovisbackend.mappers.UserMapper;
import com.astrovis.astrovisbackend.model.User;
import com.astrovis.astrovisbackend.model.UserExample;
import com.astrovis.astrovisbackend.wrapper.UserWrapper;
import io.jsonwebtoken.Claims;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;



@Service("userService")
public class UserService extends BaseService<UserMapper,User,UserExample> implements UserDetailsService{

    public static final String NOT_EXIST = "user not exist";
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (StringUtils.isBlank(username)){
            throw new UsernameNotFoundException(NOT_EXIST);
        }
        UserExample userExample = new UserExample();
        userExample.createCriteria().andUsernameEqualTo(username);
        User user = selectFirstByExample(userExample);
        if (user == null){
            throw new UsernameNotFoundException(NOT_EXIST);
        }
        return new UserWrapper(user);
    }

    public UserDetails loadUserByJwtClaims(Claims claims){

        User user = new User();
        user.setUid(-1);
        user.setUsername(claims.getSubject());
        return new UserWrapper(user);


    }








}
