package jipdol2.eunstargram.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import jipdol2.eunstargram.auth.dto.request.LoginRequestDTO;
import jipdol2.eunstargram.auth.entity.Session;
import jipdol2.eunstargram.auth.entity.SessionJpaRepository;
import jipdol2.eunstargram.member.entity.Member;
import jipdol2.eunstargram.member.entity.MemberJpaRepository;
import jipdol2.eunstargram.member.entity.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MemberJpaRepository memberJpaRepository;

    @Autowired
    private SessionJpaRepository sessionJpaRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void clean(){
        memberJpaRepository.deleteAll();
    }

    private static final String COMMON_URL="/api/auth";

    @Test
    @DisplayName("로그인 : 로그인 성공")
    void loginTest1() throws Exception{
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
    @DisplayName("로그인 : 로그인 성공 후 세션 확인")
    @Transactional
    void loginTest2() throws Exception{
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
        mockMvc.perform(post(COMMON_URL+"/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andDo(print());

        Member loggedInMember = memberJpaRepository.findById(member.getId())
                .orElseThrow(RuntimeException::new);

        List<Session> sessions = loggedInMember.getSessions();

        Assertions.assertThat(loggedInMember.getSessions().size()).isEqualTo(1L);
    }

    @Test
    @DisplayName("로그인 : 로그인 성공 후 세션 응답 확인")
    void loginTest3() throws Exception{
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
        mockMvc.perform(post(COMMON_URL+"/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").isNotEmpty())
                .andDo(print());
    }

    @Test
    @DisplayName("권한 체크를 하는 API 에 접근")
    @Transactional
    void loginTest4() throws Exception{
        //given
        Member member = Member.builder()
                .memberEmail("jipdol2@gmail.com")
                .password("1234")
                .nickname("jipdol2")
                .phoneNumber("010-1111-2222")
                .birthDay("1999-01-01")
                .intro("im jipdol2")
                .build();

        Session session = member.addSession();
        memberJpaRepository.save(member);

        //expected
        mockMvc.perform(get("/api/post/foo")
                        .header("Authorization",session.getAccessToken())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }
}