package cn.com.goodlan.webvpn.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
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
import java.io.IOException;

@RestController
@RequestMapping
public class WelcomeController {

    @Autowired
    private ThymeleafViewResolver thymeleafViewResolver;

    @ResponseBody
    @GetMapping(value = {"/", "/welcome"}, produces = "text/html")
    public String welcome(HttpServletRequest request, HttpServletResponse response, Model model) {
        return alreadyLogin() ? toIndexPage(request, response, model) : toWelcomePage(request, response, model);
    }

    private String toWelcomePage(HttpServletRequest request, HttpServletResponse response, Model model) {
        ISpringTemplateEngine templateEngine = thymeleafViewResolver.getTemplateEngine();
        IWebContext webContext = new WebContext(request, response, request.getServletContext(), response.getLocale(), model.asMap());
        return templateEngine.process("welcome", webContext);
    }

    private String toIndexPage(HttpServletRequest request, HttpServletResponse response, Model model) {
        try {
            response.sendRedirect("/index");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    private boolean alreadyLogin() {
        return !(SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken);
    }

}
