package com.wangyin.autotest.http;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 首页相关的controller.
 *
 * @author chenyun.chris
 * @since 1.0
 */
@Controller
public class IndexController {

    @RequestMapping("index")
    public String index() {
        return "index";
    }

    @RequestMapping("/")
    public String welcome() {
        return "redirect:index";
    }

}
