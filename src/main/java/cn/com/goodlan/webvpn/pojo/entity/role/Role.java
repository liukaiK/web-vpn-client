package cn.com.goodlan.webvpn.pojo.entity.role;

import cn.com.goodlan.webvpn.pojo.entity.AbstractEntity;
import cn.com.goodlan.webvpn.pojo.entity.navigation.Navigation;
import cn.com.goodlan.webvpn.pojo.entity.user.User;
import cn.hutool.core.collection.CollectionUtil;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 角色实体
 *
 * @author liukai
 */
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "resource_role")
public class Role extends AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    /**
     * 备注
     */
    private String description;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "resource_role_navigation", joinColumns = @JoinColumn(name = "role_id"), inverseJoinColumns = @JoinColumn(name = "navigation_id"))
    private List<Navigation> navigations = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "resource_user_role", joinColumns = @JoinColumn(name = "role_id"), inverseJoinColumns = @JoinColumn(name = "user_id"))
    private List<User> users = new ArrayList<>();


    protected Role() {

    }

    public Role(Long id) {
        this.id = id;
    }

    public void updateName(String name) {
        this.name = name;
    }

    public void updateDescription(String description) {
        this.description = description;
    }

    public void updateNavigations(List<Navigation> navigations) {
        this.navigations = navigations;
    }

    /**
     * 角色下面是否有人
     */
    public boolean hasUser() {
        return CollectionUtil.isNotEmpty(this.users);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public List<Navigation> getNavigations() {
        return navigations;
    }

}
