package cn.com.goodlan.webvpn.repository.navigation;

import cn.com.goodlan.webvpn.pojo.entity.navigation.Navigation;
import cn.com.goodlan.webvpn.pojo.entity.role.Role;
import cn.com.goodlan.webvpn.repository.CustomizeRepository;

import java.util.List;
import java.util.Set;

public interface NavigationRepository extends CustomizeRepository<Navigation, Long> {


    Set<Navigation> findAllByRolesIn(List<Role> roles);

}
