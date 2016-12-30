package com.golubets.monitor.environment.web.config;

import com.golubets.monitor.environment.dao.UserDao;
import com.golubets.monitor.environment.dao.UserRoleDao;
import com.golubets.monitor.environment.model.User;
import com.golubets.monitor.environment.model.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by golubets on 28.12.2016.
 */
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserDao userDao = new UserDao();

    @Autowired
    private UserRoleDao userRoleDao = new UserRoleDao();

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userDao.getByName(username);
        UserDetails userDetails = new org.springframework.security.core.userdetails.User(user.getUserName(),
                user.getPassword(),
                true, true, true,true,
                getGrantedAuthorities(user));
        return userDetails;
    }

    private List<GrantedAuthority> getGrantedAuthorities(User user) {
        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        for (UserRole userRole : userRoleDao.getRoleByUserId(user.getId())) {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + userRole.getRole().toUpperCase()));
        }
        return authorities;
    }
}
