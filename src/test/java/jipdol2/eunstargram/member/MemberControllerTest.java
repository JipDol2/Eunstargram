package jipdol2.eunstargram.member;

import com.fasterxml.jackson.databind.ObjectMapper;
import jipdol2.eunstargram.member.dto.request.MemberUpdateRequestDTO;
import jipdol2.eunstargram.member.entity.Member;
import jipdol2.eunstargram.member.entity.MemberJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.assertj.core.api.Assertions.assertThat;

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

    @PersistenceContext
    private EntityManager entityManager;

    private static final String COMMON_URL="/api/member";

    @BeforeEach
    void clean(){
        this.entityManager
                .createNativeQuery("ALTER TABLE MEMBER AUTO_INCREMENT = 1")
                .executeUpdate();
    }

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

        //given
        Member member = createMember();
        memberJpaRepository.save(member);

        Long id = 1L;
        MemberUpdateRequestDTO memberB = new MemberUpdateRequestDTO();
        memberB.setPassword("4321");
        memberB.setNickName("Rabbit99");

        ObjectMapper objectMapper = new ObjectMapper();

        //when
        mockMvc.perform(MockMvcRequestBuilders.patch(COMMON_URL + "/update/"+ id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(memberB))
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());

        //then
        Member findMember = memberJpaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));
        assertThat(findMember.getPassword()).isEqualTo(memberB.getPassword());
    }

    @Test
    @DisplayName("/delete/{id} 요청시 200 status code 리턴")
    @Transactional
    void deleteTest() throws Exception {
        //given
        Member member = createMember();
        memberJpaRepository.save(member);
        //when
        Long id=1L;
        mockMvc.perform(MockMvcRequestBuilders.patch(COMMON_URL+"/delete/"+id)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
        //then
        Member findMember = memberJpaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));
        assertThat(findMember.getDeleteYn()).isEqualTo("N");
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