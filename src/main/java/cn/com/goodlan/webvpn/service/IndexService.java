package cn.com.goodlan.webvpn.service;

import cn.com.goodlan.webvpn.pojo.entity.navigation.Navigation;
import cn.com.goodlan.webvpn.pojo.entity.proxy.Proxy;
import cn.com.goodlan.webvpn.pojo.entity.role.Role;
import cn.com.goodlan.webvpn.pojo.vo.IndexVO;
import cn.com.goodlan.webvpn.pojo.vo.ProxyVO;
import cn.com.goodlan.webvpn.repository.domain.DomainRepository;
import cn.com.goodlan.webvpn.repository.navigation.NavigationRepository;
import cn.com.goodlan.webvpn.repository.role.RoleRepository;
import cn.com.goodlan.webvpn.repository.user.UserRepository;
import cn.com.goodlan.webvpn.utils.SecurityUtil;
import cn.hutool.core.util.URLUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class IndexService {

    @Autowired
    private NavigationRepository navigationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DomainRepository domainRepository;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private RoleRepository roleRepository;


    @Transactional
    public List<IndexVO> index() {

        String domain = redisTemplate.opsForValue().get("domain");

        List<IndexVO> indexVOS = new ArrayList<>();

        List<Role> roles = new ArrayList<>();
        Long[] roleIds = SecurityUtil.getUser().getRoleIds();
        for (Long roleId : roleIds) {
            roles.add(new Role(roleId));
        }


        Set<Navigation> navigations = navigationRepository.findAllByRolesIn(roles);

        for (Navigation navigation : navigations) {
            IndexVO indexVO = new IndexVO();
            indexVO.setNavigationName(navigation.getName());

            List<ProxyVO> proxyVOS = new ArrayList<>();
            for (Proxy proxy : navigation.getProxies()) {

                String path = URLUtil.toURI(proxy.getProtocol() + "://" + proxy.getUrl()).getPath();

                ProxyVO proxyVO = new ProxyVO();
                proxyVO.setName(proxy.getName());
                String virtualDomain = proxy.getProtocol() + "://" + proxy.getVirtualDomain() + "." + domain + path;
                proxyVO.setUrl(virtualDomain);
                proxyVOS.add(proxyVO);
            }
            indexVO.setProxyVOs(proxyVOS);
            indexVOS.add(indexVO);
        }


        return indexVOS;
    }

}
