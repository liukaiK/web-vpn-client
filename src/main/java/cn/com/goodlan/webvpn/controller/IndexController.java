package cn.com.goodlan.webvpn.controller;

import cn.com.goodlan.webvpn.service.IndexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping
public class IndexController {

    @Autowired
    private IndexService indexService;

    @GetMapping({"/", "/index"})
    public ModelAndView add(Model model) {
        model.addAttribute("datas", indexService.index());
        return new ModelAndView("index");
    }

}
