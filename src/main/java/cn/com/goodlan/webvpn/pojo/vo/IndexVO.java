package cn.com.goodlan.webvpn.pojo.vo;

import java.util.List;

public class IndexVO {

    private String navigationName;


    private List<ProxyVO> proxyVOs;

    public String getNavigationName() {
        return navigationName;
    }

    public void setNavigationName(String navigationName) {
        this.navigationName = navigationName;
    }

    public List<ProxyVO> getProxyVOs() {
        return proxyVOs;
    }

    public void setProxyVOs(List<ProxyVO> proxyVOs) {
        this.proxyVOs = proxyVOs;
    }
}
