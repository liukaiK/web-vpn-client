package cn.com.goodlan.webvpn.controller;

import cn.com.goodlan.webvpn.repository.role.RoleRepository;
import cn.com.goodlan.webvpn.service.IndexService;
import cn.com.goodlan.webvpn.utils.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.thymeleaf.context.IWebContext;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.spring5.ISpringTemplateEngine;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping
public class IndexController {

    @Autowired
    private IndexService indexService;

    @Autowired
    private ThymeleafViewResolver thymeleafViewResolver;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @ResponseBody
    @GetMapping(value = {"/", "/index"}, produces = "text/html")
    public String index(HttpServletRequest request, HttpServletResponse response, Model model) {

        Long[] roleIds = SecurityUtil.getUser().getRoleIds();

        String hashKey = StringUtils.arrayToDelimitedString(roleIds, ":");
        Boolean exists = redisTemplate.opsForHash().hasKey("cachehtml", hashKey);
        if (exists) {
            return redisTemplate.opsForHash().get("cachehtml", hashKey).toString();
        }


        model.addAttribute("datas", indexService.index());
        ISpringTemplateEngine templateEngine = thymeleafViewResolver.getTemplateEngine();

        IWebContext webContext = new WebContext(request, response, request.getServletContext(), response.getLocale(), model.asMap());
        String cacheHtml = templateEngine.process("index", webContext);

        redisTemplate.opsForHash().put("cachehtml", hashKey, cacheHtml);


        return cacheHtml;

    }

}
