package jipdol2.eunstargram.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import jipdol2.eunstargram.auth.dto.request.LoginRequestDTO;
import jipdol2.eunstargram.crypto.MyPasswordEncoder;
import jipdol2.eunstargram.member.entity.Member;
import jipdol2.eunstargram.member.entity.MemberJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
//@Transactional
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MemberJpaRepository memberJpaRepository;

    @Autowired
    private MyPasswordEncoder myPasswordEncoder;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void clean(){
        memberJpaRepository.deleteAll();
    }

    private static final String COMMON_URL="/api/auth";

    @Test
    @DisplayName("로그인 : 로그인 성공")
    void loginSuccessTest() throws Exception{
        //given
        memberJpaRepository.save(Member.builder()
                .memberEmail("jipdol2@gmail.com")
                .password(myPasswordEncoder.encrypt("1234"))
                .nickname("jipdol2")
                .phoneNumber("010-1111-2222")
                .birthDay("1999-01-01")
                .intro("im jipdol2")
                .build());

        LoginRequestDTO login = LoginRequestDTO.builder()
                .memberEmail("jipdol2@gmail.com")
                .password("1234")
                .build();

        String json = objectMapper.writeValueAsString(login);
        //expected
        mockMvc.perform(post(COMMON_URL+"/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(json))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("로그인 : 로그인 실패")
    void loginFailTest() throws Exception{
        //given
        memberJpaRepository.save(Member.builder()
                .memberEmail("jipdol2@gmail.com")
                .password(myPasswordEncoder.encrypt("1234"))
                .nickname("jipdol2")
                .phoneNumber("010-1111-2222")
                .birthDay("1999-01-01")
                .intro("im jipdol2")
                .build());

        LoginRequestDTO login = LoginRequestDTO.builder()
                .memberEmail("jipdol2@gmail.com")
                .password("4321")
                .build();

        String json = objectMapper.writeValueAsString(login);
        //expected
        mockMvc.perform(post(COMMON_URL+"/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("M004"))
                .andExpect(jsonPath("$.message").value("아이디/비밀번호가 올바르지 않습니다"))
                .andExpect(jsonPath("$.validation['id/password']").value("아이디/비밀번호가 올바르지 않습니다"))
                .andDo(print());
    }

    @Test
    @DisplayName("로그인 : 이메일은 필수입니다")
    void loginEmailTest() throws Exception{
        //given
        memberJpaRepository.save(Member.builder()
                .memberEmail("jipdol2@gmail.com")
                .password("1234")
                .nickname("jipdol2")
                .phoneNumber("010-1111-2222")
                .birthDay("1999-01-01")
                .intro("im jipdol2")
                .build());

        LoginRequestDTO login = LoginRequestDTO.builder()
                .memberEmail(" ")
                .password("1234")
                .build();

        String json = objectMapper.writeValueAsString(login);
        //expected
        mockMvc.perform(post(COMMON_URL+"/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.message").value("Bad Request"))
                .andExpect(jsonPath("$.validation.memberEmail").value("올바른 이메일 형식을 입력해주세요"))
                .andDo(print());
    }

    @Test
    @DisplayName("로그인 : 비밀번호는 필수입니다")
    void loginPasswordTest() throws Exception{
        //given
        memberJpaRepository.save(Member.builder()
                .memberEmail("jipdol2@gmail.com")
                .password("1234")
                .nickname("jipdol2")
                .phoneNumber("010-1111-2222")
                .birthDay("1999-01-01")
                .intro("im jipdol2")
                .build());

        LoginRequestDTO login = LoginRequestDTO.builder()
                .memberEmail("jipdol2@gmail.com")
                .password(" ")
                .build();

        String json = objectMapper.writeValueAsString(login);
        //expected
        mockMvc.perform(post(COMMON_URL+"/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.message").value("Bad Request"))
                .andExpect(jsonPath("$.validation.password").value("비밀번호를 입력해주세요"))
                .andDo(print());
    }

    @Test
    @DisplayName("로그인 : 로그인 성공 후 세션 응답 확인")
    @Disabled
    void loginSessionTest() throws Exception{
        //given
        Member member = memberJpaRepository.save(Member.builder()
                .memberEmail("jipdol2@gmail.com")
                .password("1234")
                .nickname("jipdol2")
                .phoneNumber("010-1111-2222")
                .birthDay("1999-01-01")
                .intro("im jipdol2")
                .build());

        LoginRequestDTO login = LoginRequestDTO.builder()
                .memberEmail("jipdol2@gmail.com")
                .password("1234")
                .build();

        String json = objectMapper.writeValueAsString(login);
        //expected
        mockMvc.perform(post(COMMON_URL+"/v0/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.accessToken").isNotEmpty())
                .andExpect(MockMvcResultMatchers.cookie().exists("SESSION"))
                .andDo(print());
    }

}