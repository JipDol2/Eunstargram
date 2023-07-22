package jipdol2.eunstargram.common;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jipdol2.eunstargram.auth.entity.NoAuth;
import jipdol2.eunstargram.config.data.UserSession;
import jipdol2.eunstargram.image.dto.response.ImageResponseDTO;
import jipdol2.eunstargram.member.MemberService;
import jipdol2.eunstargram.member.dto.response.MemberFindResponseDTO;
import jipdol2.eunstargram.post.PostService;
import jipdol2.eunstargram.post.dto.response.PostResponseDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.annotation.AuthenticationPrincipal;
//import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
//import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.RequestContextUtils;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequiredArgsConstructor
@NoAuth
public class IndexController {

    private final MemberService memberService;
    private final PostService postService;

//    @ResponseBody
    @GetMapping("/")
    public String index(Model model){
//        OAuth2AuthenticationToken authenticationToken = (OAuth2AuthenticationToken)authentication;
//        if(authentication != null){
//            Map<String, Object> attributes = oAuth2User.getAttributes();
//            model.addAttribute("loginId",String.valueOf(attributes.get("login")));
//        }
        return "index";
    }

    @GetMapping("/login")
    public String login(Model model){
//        model.addAttribute("id","1");
        return "/login";
    }

    @GetMapping("/redirect/login")
    public String loginRedirect(){
        return "/redirect/login";
    }

    @ResponseBody
    @GetMapping("/user")
    public String user(){
        return "사용자 페이지 입니다.";
    }

    @ResponseBody
    @GetMapping("/admin")
    public String admin(){
        return "관리자 페이지 입니다.";
    }

    @GetMapping("/signUp")
    public String singUp(){
        return "/signUp";
    }

    @GetMapping("/posts/{nickname}")
    public String profile(@PathVariable("nickname") String nickname,Model model){
        log.info("nickname={}",nickname);
        MemberFindResponseDTO findByMember = memberService.findByMemberNickname(nickname);
        model.addAttribute("member",findByMember);

        ImageResponseDTO findByProfileImage = memberService.findByProfileImage(nickname);
        model.addAttribute("profileImage",findByProfileImage);

        List<PostResponseDTO> findByPosts = postService.findByAll(nickname);
        model.addAttribute("posts",findByPosts);
        return "/posts";
    }

    @GetMapping("/signUp-social")
    public String signUpSocial(){
        return "/signUp-social";
    }

    @GetMapping("/oauthCallback")
    public String oauthCallback(HttpServletRequest request){
        Map<String, ?> inputFlashMap = RequestContextUtils.getInputFlashMap(request);
        String code = (String) inputFlashMap.get("code");
        log.info(">>>code={}",code);
        return "/oauthCallback";
    }
}
