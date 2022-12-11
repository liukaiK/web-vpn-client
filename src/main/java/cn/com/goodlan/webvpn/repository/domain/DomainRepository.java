package cn.com.goodlan.webvpn.repository.domain;

import cn.com.goodlan.webvpn.pojo.entity.domain.Domain;
import cn.com.goodlan.webvpn.repository.CustomizeRepository;

public interface DomainRepository extends CustomizeRepository<Domain, Long> {

    Domain getTopByOrderByUpdateTimeDesc();

}
