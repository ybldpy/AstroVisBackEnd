package com.astrovis.astrovisbackend.services;


import com.astrovis.astrovisbackend.mappers.UserMapper;
import com.astrovis.astrovisbackend.model.User;
import com.astrovis.astrovisbackend.model.UserExample;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service("userAuthService")
public class UserDetailsServiceImpl extends BaseService<UserMapper,User,UserExample> implements UserDetailsService{



    private static final String NOT_EXIT = "user not exist";

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserExample userExample = new UserExample();
        userExample.createCriteria().andUsernameEqualTo(username);
        User user = selectFirstByExample(userExample);
        if (user == null){
            throw new UsernameNotFoundException(NOT_EXIT);
        }
        return user;
    }
}
