package cn.com.goodlan.webvpn.service;

import cn.com.goodlan.webvpn.pojo.entity.user.User;
import cn.com.goodlan.webvpn.repository.user.UserRepository;
import cn.com.goodlan.webvpn.security.userdetails.SecurityUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserCache;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private static final Logger log = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserCache userCache;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        UserDetails userFromCache = userCache.getUserFromCache(username);

        if (userFromCache != null) {
            log.info("从缓存中查找出用户{}", userFromCache);
            return userFromCache;
        }

        User user = userRepository.getByUsername(username).orElseThrow(() -> new UsernameNotFoundException(username + "不存在"));

        SecurityUser securityUser = SecurityUser.convertFromUser(user);

        userCache.putUserInCache(securityUser);

        return securityUser;


    }


}
