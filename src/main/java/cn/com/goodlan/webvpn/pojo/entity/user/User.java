package cn.com.goodlan.webvpn.pojo.entity.user;

import cn.com.goodlan.webvpn.pojo.entity.AbstractEntity;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

/**
 * 学校的用户
 *
 * @author liukai
 */
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "resource_user")
public class User extends AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String username;

    /**
     * 冗余字段 以/分割
     */
    private String roleIds;

    /**
     * 冗余字段 以/分割
     */
    private String roleNames;

    protected User() {
    }

    public User(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }

    public String getRoleIds() {
        return roleIds;
    }

    public String getRoleNames() {
        return roleNames;
    }

}
