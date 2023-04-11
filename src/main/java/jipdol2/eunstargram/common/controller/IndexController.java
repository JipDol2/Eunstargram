package jipdol2.eunstargram.common.controller;

import jipdol2.eunstargram.config.data.UserSession;
import jipdol2.eunstargram.post.PostService;
import jipdol2.eunstargram.post.dto.response.PostResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class IndexController {

    private final PostService postService;

    @GetMapping
    public String index(){
        return "index";
    }

    @GetMapping("/login")
    public String login(Model model){
//        model.addAttribute("id","1");
        return "/login";
    }

    @GetMapping("/signUp")
    public String singUp(){
        return "/signUp";
    }

    @GetMapping("/posts")
    public String profile(){
//        log.info("id={}",id);
        return "/posts";
    }

    @GetMapping("/posts/{memberId}")
    public String profile(@PathVariable("memberId") Long id){
        log.info("id={}",id);
        return "/posts";
    }
}
