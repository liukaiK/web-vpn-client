package cn.com.goodlan.webvpn.pojo.entity.domain;

import cn.com.goodlan.webvpn.pojo.entity.AbstractEntity;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

/**
 * 一级域名
 */
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "system_domain")
public class Domain extends AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String domain;

    public Long getId() {
        return id;
    }

    public String getDomain() {
        return domain;
    }

}
