package jipdol2.eunstargram.member;

import com.fasterxml.jackson.databind.ObjectMapper;
import jipdol2.eunstargram.member.dto.request.MemberLoginRequestDTO;
import jipdol2.eunstargram.member.dto.request.MemberSaveRequestDTO;
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
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.io.FileInputStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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

    @Autowired
    private ObjectMapper objectMapper;

    private static final String COMMON_URL="/api/member";

    @BeforeEach
    void clean(){
        this.entityManager
                .createNativeQuery("ALTER TABLE MEMBER AUTO_INCREMENT = 1")
                .executeUpdate();
    }

    @Test
    @DisplayName("회원가입 : /api/member/signUp 요청시 200 status code 리턴")
    @Transactional
    void signUpTest() throws Exception {

        MemberSaveRequestDTO memberSaveRequestDTO = MemberSaveRequestDTO.builder()
                        .memberEmail("jipdol2@gmail.com")
                        .password("1234")
                        .nickName("Rabbit96")
                        .phoneNumber("010-1111-2222")
                        .birthDay("20220107")
                        .intro("Life is just one")
                        .build();

        String json = objectMapper.writeValueAsString(memberSaveRequestDTO);

        //expected
        mockMvc.perform(MockMvcRequestBuilders.post(COMMON_URL + "/signUp")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(status().isOk())
                .andDo(print());

        assertThat(memberJpaRepository.count()).isEqualTo(1);
    }

    @Test
    @DisplayName("로그인 : /api/member/login 요청시 200 status + true 리턴")
    @Transactional
    void loginTest() throws Exception {

        //given
        Member member = createMember();
        memberJpaRepository.save(member);

        MemberLoginRequestDTO memberLoginRequestDTO = MemberLoginRequestDTO.builder()
                .memberEmail("jipdol2@gmail.com")
                .password("1234")
                .build();

        String json = objectMapper.writeValueAsString(memberLoginRequestDTO);

        //expected
        mockMvc.perform(MockMvcRequestBuilders.post(COMMON_URL + "/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andDo(print());
    }

    @Test
    @DisplayName("회원정보수정 : /api/member/update/{id} 요청시 200 status code 리턴")
    @Transactional
    void updateTest() throws Exception{

        //given
        Member member = createMember();
        memberJpaRepository.save(member);

        Long id = 1L;
        MemberUpdateRequestDTO memberB = MemberUpdateRequestDTO.builder()
                        .password("4321")
                        .nickName("Rabbit99")
                        .build();

        //when
        mockMvc.perform(MockMvcRequestBuilders.patch(COMMON_URL + "/update/"+ id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(memberB))
                )
                .andExpect(status().isOk())
                .andDo(print());

        //then
        Member findMember = memberJpaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));
        assertThat(findMember.getPassword()).isEqualTo(memberB.getPassword());
    }

    @Test
    @DisplayName("회원탈퇴 : /api/member/delete/{id} 요청시 200 status code 리턴")
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
                .andExpect(status().isOk())
                .andDo(print());
        //then
        Member findMember = memberJpaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));
        assertThat(findMember.getDeleteYn()).isEqualTo("N");
    }

    @Test
    @DisplayName("회원 전체조회 : /api/member/ 요청시 200 status code 와 회원정보들 리턴")
    @Transactional
    void findByAllMemberTest() throws Exception {
        //expect
        mockMvc.perform(MockMvcRequestBuilders.get(COMMON_URL+"/"))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("회원 조회 : /api/member/{id} 요청시 200 status code 와 회원정보 리턴")
    @Transactional
    void findByMemberTest() throws Exception {
        //given
        Member member = createMember();
        memberJpaRepository.save(member);
        //when
        mockMvc.perform(MockMvcRequestBuilders.get(COMMON_URL+"/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.memberEmail").value("jipdol2@gmail.com"))
                .andExpect(jsonPath("$.password").value("1234"))
                .andDo(print());

    }

    @Test
    @DisplayName("회원 프로필 이미지 업로드 : /api/member/profileImage 200 status code 리턴")
    @Transactional
    void uploadProfileImageTest() throws Exception{

        Member member = createMember();
        memberJpaRepository.save(member);

        String originalFilename = "testImage.jpg";
        String filePath = "src/test/resources/img/" + originalFilename;

        MockMultipartFile mockImage = new MockMultipartFile("image", originalFilename, "image/jpg", new FileInputStream(filePath));

        mockMvc.perform(MockMvcRequestBuilders.multipart(COMMON_URL+"/profileImage")
                .file(mockImage)
                )
                .andExpect(status().isOk())
                .andDo(print());
    }

    private Member createMember() {
        Member member = Member.builder()
                .memberEmail("jipdol2@gmail.com")
                .password("1234")
                .nickname("Rabbit96")
                .birthDay("19940715")
                .intro("life is one time")
                .build();
        return member;
    }
}