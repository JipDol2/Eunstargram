package jipdol2.eunstargram.common.controller;

import jipdol2.eunstargram.post.PostService;
import jipdol2.eunstargram.post.dto.response.PostResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class IndexController {

    private final PostService postService;

    @GetMapping
    public String index(){
        return "index";
    }

    @GetMapping("/login")
    public String login(){
        return "/login";
    }

    @GetMapping("/signUp")
    public String singUp(){
        return "/signUp";
    }

    @GetMapping("/{memberId}")
    public String profile(Model model, @PathVariable("memberId") String memberId){
        List<PostResponseDTO> posts = postService.findByAll(memberId);
        model.addAttribute("posts",posts);
        return "posts";
    }
}
