package cn.com.goodlan.webvpn.pojo.entity.navigation;

import cn.com.goodlan.webvpn.pojo.entity.AbstractEntity;
import cn.com.goodlan.webvpn.pojo.entity.proxy.Proxy;
import cn.com.goodlan.webvpn.pojo.entity.role.Role;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 导航
 *
 * @author liukai
 */
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "resource_navigation")
public class Navigation extends AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String proxyIds;

    private String proxyNames;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "resource_role_navigation", joinColumns = @JoinColumn(name = "navigation_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private List<Role> roles = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "resource_proxy_navigation", joinColumns = @JoinColumn(name = "navigation_id"), inverseJoinColumns = @JoinColumn(name = "proxy_id"))
    private List<Proxy> proxies = new ArrayList<>();


    protected Navigation() {
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getProxyIds() {
        return proxyIds;
    }

    public String getProxyNames() {
        return proxyNames;
    }

    public List<Proxy> getProxies() {
        return proxies;
    }

    public List<Role> getRoles() {
        return roles;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Navigation that = (Navigation) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
