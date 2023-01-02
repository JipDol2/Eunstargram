package jipdol2.eunstargram.common.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {

    @GetMapping("/")
    public String index(){
        return "index";
    }

    @GetMapping("login")
    public String login(){
        return "/login";
    }

    @GetMapping("singUp")
    public String singUp(){
        return "/singUp";
    }
}
