package cn.com.goodlan.webvpn.service;

import cn.com.goodlan.webvpn.pojo.entity.domain.Domain;
import cn.com.goodlan.webvpn.pojo.entity.navigation.Navigation;
import cn.com.goodlan.webvpn.pojo.entity.proxy.Proxy;
import cn.com.goodlan.webvpn.pojo.vo.IndexVO;
import cn.com.goodlan.webvpn.pojo.vo.ProxyVO;
import cn.com.goodlan.webvpn.repository.domain.DomainRepository;
import cn.com.goodlan.webvpn.repository.navigation.NavigationRepository;
import cn.com.goodlan.webvpn.repository.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class IndexService {

    @Autowired
    private NavigationRepository navigationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DomainRepository domainRepository;


    @Transactional
    public List<IndexVO> index() {

        Domain domain = domainRepository.getTopByOrderByUpdateTimeDesc();

        List<IndexVO> indexVOS = new ArrayList<>();

        List<Navigation> navigations = navigationRepository.findAll();

        for (Navigation navigation : navigations) {
            IndexVO indexVO = new IndexVO();
            indexVO.setNavigationName(navigation.getName());

            List<ProxyVO> proxyVOS = new ArrayList<>();
            for (Proxy proxy : navigation.getProxies()) {
                ProxyVO proxyVO = new ProxyVO();
                proxyVO.setName(proxy.getName());
                String virtualDomain = proxy.getProtocol() + "://" + proxy.getVirtualDomain() + "." + domain.getDomain();
                proxyVO.setUrl(virtualDomain);
                proxyVOS.add(proxyVO);
            }
            indexVO.setProxyVOs(proxyVOS);
            indexVOS.add(indexVO);
        }
        return indexVOS;
    }

}
