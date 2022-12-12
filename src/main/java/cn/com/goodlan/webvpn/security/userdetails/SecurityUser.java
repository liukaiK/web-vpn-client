package cn.com.goodlan.webvpn.security.userdetails;

import cn.com.goodlan.webvpn.pojo.entity.user.User;
import cn.hutool.core.convert.Convert;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * 存放在SpringSecurity中的实体用户对象
 *
 * @author liukai
 */
public class SecurityUser implements UserDetails, Serializable {

    private Long id;

    private String name;

    private String username;

    private String password;

    private Long[] roleIds;

    private String roleNames;

    private List<SecurityRole> securityRole;

//    private List<SecurityAuthority> authorities;

    public static SecurityUser convertFromUser(User user) {
        return new SecurityUser(user);
    }

    private SecurityUser(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.username = user.getUsername();
        this.password = "";
        this.roleIds = splitAndSort(user.getRoleIds());
        this.roleNames = user.getRoleNames();
    }

    private Long[] splitAndSort(String roleIds) {
        Long[] ids = Convert.toLongArray(roleIds.split("/"));
        Arrays.sort(ids);
        return ids;
    }

//    private List<SecurityRole> obtainRoles(List<Role> roles) {
//        List<SecurityRole> roles = new ArrayList<>();
//        for (Role role : roles) {
//            roles.add(new SecurityRole(role));
//        }
//        return roles;
//    }

//    private List<SecurityAuthority> obtainAuthorities(List<SystemRole> roleList) {
//        List<SecurityAuthority> grantedAuthorities = new ArrayList<>();
//        for (SystemRole role : roleList) {
//            List<Menu> menuList = role.getMenus();
//            for (Menu menu : menuList) {
//                grantedAuthorities.add(SecurityAuthority.convertFormMenu(menu));
//            }
//        }
//        return grantedAuthorities;
//    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
    }

//    public Admin castToUser() {
//        return new Admin(this.id);
//    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<SecurityRole> getSecurityRole() {
        return securityRole;
    }

    public void setSecurityRole(List<SecurityRole> securityRole) {
        this.securityRole = securityRole;
    }

    public Long[] getRoleIds() {
        return roleIds;
    }

    public String getRoleNames() {
        return roleNames;
    }

    //    public void setAuthorities(List<SecurityAuthority> authorities) {
//        this.authorities = authorities;
//    }

}
