package cn.com.goodlan.webvpn.service.cache;

import cn.com.goodlan.webvpn.pojo.entity.user.User;
import cn.com.goodlan.webvpn.security.userdetails.SecurityUser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.userdetails.UserCache;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserCacheImpl implements UserCache {

    private static final Logger log = LoggerFactory.getLogger(UserCacheImpl.class);

    private String userCacheKey = "user";

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public UserDetails getUserFromCache(String username) {
        Object userCache = redisTemplate.opsForHash().get(userCacheKey, username);
        if (userCache == null) {
            return null;
        }
        String userJson = userCache.toString();
        try {
            User user = objectMapper.readValue(userJson, User.class);
            return SecurityUser.convertFromUser(user);
        } catch (JsonProcessingException e) {
            log.error("json转user失败: {}", userJson);
            throw new RuntimeException(e);
        }

    }

    @Override
    public void putUserInCache(UserDetails user) {

        SecurityUser securityUser = (SecurityUser) user;

        Map<String, Object> map = new HashMap<>();
        map.put("id", securityUser.getId());
        map.put("name", securityUser.getName());
        map.put("username", securityUser.getUsername());
        map.put("roleIds", securityUser.getRoleIds());
        map.put("roleNames", securityUser.getRoleNames());
        try {
            redisTemplate.opsForHash().put(userCacheKey, securityUser.getUsername(), objectMapper.writeValueAsString(map));
        } catch (JsonProcessingException e) {
            log.error("user转json失败: {}", securityUser);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void removeUserFromCache(String username) {

    }


}
