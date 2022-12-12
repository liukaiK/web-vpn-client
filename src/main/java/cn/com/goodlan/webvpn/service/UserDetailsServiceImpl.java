package cn.com.goodlan.webvpn.service;

import cn.com.goodlan.webvpn.pojo.entity.user.User;
import cn.com.goodlan.webvpn.repository.user.UserRepository;
import cn.com.goodlan.webvpn.security.userdetails.SecurityUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.getByUsername(username);
        return SecurityUser.convertFromUser(user);
    }


}
