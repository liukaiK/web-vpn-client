package cn.com.goodlan.webvpn.service;


import cn.com.goodlan.webvpn.ClientApplication;
import cn.com.goodlan.webvpn.pojo.vo.IndexVO;
import cn.hutool.core.util.URLUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.net.URI;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ClientApplication.class)
@WithUserDetails(value = "2007008", userDetailsServiceBeanName = "userDetailsServiceImpl")
public class IndexServiceTest {

    @Autowired
    private IndexService indexService;

    @Test
    @Transactional
    @Rollback(value = false)
    public void index() {

        List<IndexVO> index = indexService.index();


    }

    @Test
    public void testURLUtil() {
        String url = "http://my.webvpn2.hrbfu.edu.cn/index.portal";
        URI uri = URLUtil.toURI(url, false);

        System.out.println(uri.getPath());
    }
}