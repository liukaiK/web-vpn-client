package cn.com.goodlan.webvpn.repository.user;

import cn.com.goodlan.webvpn.pojo.entity.user.User;
import cn.com.goodlan.webvpn.repository.CustomizeRepository;

import java.util.Optional;

public interface UserRepository extends CustomizeRepository<User, Long> {

    boolean existsByUsername(String username);

    boolean existsByUsernameAndIdNot(String username, Long userId);

    Optional<User> getByUsername(String username);

}
