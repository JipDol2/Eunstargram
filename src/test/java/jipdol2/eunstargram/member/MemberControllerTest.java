package jipdol2.eunstargram.member;

import com.fasterxml.jackson.databind.ObjectMapper;
import jipdol2.eunstargram.member.dto.request.MemberSaveRequestDTO;
import jipdol2.eunstargram.member.dto.request.MemberUpdateRequestDTO;
import jipdol2.eunstargram.member.entity.Member;
import jipdol2.eunstargram.member.entity.MemberJpaRepository;
import jipdol2.eunstargram.member.entity.MemberRepository;
import org.aspectj.lang.annotation.After;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.given;

//@WebMvcTest(MemberController.class)
@AutoConfigureMockMvc
@SpringBootTest
class MemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberJpaRepository memberJpaRepository;

    private static final String COMMON_URL="/api/member";

/*    @BeforeEach
    void clean(){
        memberJpaRepository.deleteAll();
    }*/

    @Test
    @DisplayName("/signUp 요청시 200 status code 리턴")
    @Transactional
    void signUpTest() throws Exception {
        //expected
        mockMvc.perform(MockMvcRequestBuilders.post(COMMON_URL + "/signUp")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"memberId\" : \"testId\"," +
                                "\"password\" : \"1234\"," +
                                "\"nickName\" : \"Rabbit96\"," +
                                "\"phoneNumber\" : \"010-1111-2222\"," +
                                "\"birthDay\" : \"20220107\"," +
                                "\"intro\" : \"AA\"," +
                                "\"imagePath\" : \"location/1234\"," +
                                "\"cancelYN\" : \"N\"}")
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());

        assertThat(memberJpaRepository.count()).isEqualTo(1);
    }

    @Test
    @DisplayName("/login 요청시 200 status + true 리턴")
    @Transactional
    void loginTest() throws Exception {

        //given
        Member member = createMember();
        memberJpaRepository.save(member);
        //expected
        mockMvc.perform(MockMvcRequestBuilders.post(COMMON_URL + "/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"memberId\" : \"testId\"," +
                                "\"password\" : \"1234\"}")
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("true"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("/update/{id} 요청시 200 status code 리턴")
    @Transactional
    void updateTest() throws Exception{

        Member member = createMember();
        memberJpaRepository.save(member);

        Long id = 1L;
        MemberUpdateRequestDTO memberB = new MemberUpdateRequestDTO();
        memberB.setPassword("4321");
        memberB.setNickName("Rabbit99");

        ObjectMapper objectMapper = new ObjectMapper();

        mockMvc.perform(MockMvcRequestBuilders.patch(COMMON_URL + "/update/"+ id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(memberB)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());

        Member findMember = memberJpaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));
        assertThat(findMember.getPassword()).isEqualTo(memberB.getPassword());
    }

    private Member createMember() {
        Member member = Member.builder()
                .memberId("testId")
                .password("1234")
                .nickname("Rabbit96")
                .birthDay("19940715")
                .intro("life is one time")
                .imagePath("D:/save")
                .deleteYn("N")
                .build();
        return member;
    }
}