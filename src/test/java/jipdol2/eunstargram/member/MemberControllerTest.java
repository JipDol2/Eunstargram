package jipdol2.eunstargram.member;

import com.fasterxml.jackson.databind.ObjectMapper;
import jipdol2.eunstargram.auth.AuthController;
import jipdol2.eunstargram.exception.MemberNotFound;
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
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.FileInputStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//@WebMvcTest(MemberController.class)
@AutoConfigureMockMvc
@SpringBootTest
@Transactional
class MemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberJpaRepository memberJpaRepository;

    @Autowired
    private AuthController authController;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String COMMON_URL="/api/member";

    @BeforeEach
    void init(){
        memberJpaRepository.deleteAll();
    }

    //TODO: 암호화한 password 테스트 진행
    @Test
    @DisplayName("회원가입 : /api/member/signUp 요청시 200 status code 리턴")
    void signUpTest() throws Exception {

        MemberSaveRequestDTO memberSaveRequestDTO = createMemberSaveRequestDTO(
                "jipdol2@gmail.com",
                "1234",
                "Rabbit96",
                "im Rabbit96!!",
                "19940715",
                "life is one time");

        String json = objectMapper.writeValueAsString(memberSaveRequestDTO);

        //expected
        mockMvc.perform(post(COMMON_URL + "/signUp")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(status().isOk())
                .andDo(print());

        Member findByMember = memberJpaRepository.findById(1L)
                .orElseThrow(() -> new MemberNotFound());

        assertThat(findByMember.getId()).isEqualTo(findByMember.getId());
        assertThat(memberJpaRepository.count()).isEqualTo(1);
    }

    @Test
    @DisplayName("회원가입 : 올바른 이메일 형식을 입력해야 합니다")
    void signUpEmailTest() throws Exception{

        MemberSaveRequestDTO memberSaveRequestDTO = createMemberSaveRequestDTO(
                " jipdol2$naver.com",
                "1234",
                "Rabbit96",
                "im Rabbit96!!",
                "19940715",
                "life is one time");

        String json = objectMapper.writeValueAsString(memberSaveRequestDTO);

        // expected
        mockMvc.perform(post(COMMON_URL + "/signUp")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.message").value("Bad Request"))
                .andExpect(jsonPath("$.validation.memberEmail").value("올바른 이메일 형식을 적어주세요"))
                .andDo(print());
    }

    @Test
    @DisplayName("회원가입 : 비밀번호값은 필수 입니다")
    void signUpPasswordTest() throws Exception{

        MemberSaveRequestDTO memberSaveRequestDTO = createMemberSaveRequestDTO(
                "jipdol2@gmail.com",
                " ",
                "Rabbit96",
                "im Rabbit96!!",
                "19940715",
                "life is one time");

        String json = objectMapper.writeValueAsString(memberSaveRequestDTO);

        // expected
        mockMvc.perform(post(COMMON_URL + "/signUp")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
        )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.message").value("Bad Request"))
                .andExpect(jsonPath("$.validation.password").value("비밀번호를 적어주세요"))
                .andDo(print());
    }

    @Test
    @DisplayName("회원가입 : 닉네임은 필수입니다")
    void signUpNicknameTest() throws Exception{

        MemberSaveRequestDTO memberSaveRequestDTO = createMemberSaveRequestDTO(
                "jipdol2@gmail.com",
                "1234",
                " ",
                "im Rabbit96!!",
                "19940715",
                "life is one time");

        String json = objectMapper.writeValueAsString(memberSaveRequestDTO);

        // expected
        mockMvc.perform(post(COMMON_URL + "/signUp")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.message").value("Bad Request"))
                .andExpect(jsonPath("$.validation.nickname").value("닉네임을 적어주세요"))
                .andDo(print());
    }

    @Test
    @DisplayName("회원정보수정 : /api/member/update/{id} 요청시 200 status code 리턴")
    void updateTest() throws Exception{

        //given
        Member member = createMember(
                "jipdol2@gmail.com",
                "1234",
                "Rabbit96",
                "im Rabbit96!!",
                "19940715",
                "life is one time");
        Member saveMember = memberJpaRepository.save(member);

        Long id = saveMember.getId();
        MemberUpdateRequestDTO memberB = MemberUpdateRequestDTO.builder()
                        .password("4321")
                        .nickName("Rabbit99")
                        .build();

        //when
        mockMvc.perform(patch(COMMON_URL + "/update/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(memberB))
                )
                .andExpect(status().isOk())
                .andDo(print());

        //then
        Member findMember = memberJpaRepository.findById(id)
                .orElseThrow(() -> new MemberNotFound());
        assertThat(findMember.getPassword()).isEqualTo(memberB.getPassword());
    }

    @Test
    @DisplayName("회원탈퇴 : /api/member/delete/{id} 요청시 200 status code 리턴")
    void deleteTest() throws Exception {
        //given
        Member member = createMember(
                "jipdol2@gmail.com",
                "1234",
                "Rabbit96",
                "im Rabbit96!!",
                "19940715",
                "life is one time");
        Member saveMember = memberJpaRepository.save(member);
        //when
        Long id=saveMember.getId();
        mockMvc.perform(patch(COMMON_URL+"/delete/{id}",id)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andDo(print());
        //then
        Member findMember = memberJpaRepository.findById(id)
                .orElseThrow(() -> new MemberNotFound());
        assertThat(findMember.getDeleteYn()).isEqualTo("N");
    }

    @Test
    @DisplayName("회원 전체조회 : /api/member/ 요청시 200 status code 와 회원정보들 리턴")
    void findByAllMemberTest() throws Exception {
        //expect
        mockMvc.perform(get(COMMON_URL+"/"))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("회원 조회 : /api/member/{id} 요청시 200 status code 와 회원정보 리턴")
    void findByMemberTest() throws Exception {
        //given
        Member member = createMember(
                "jipdol2@gmail.com",
                "1234",
                "Rabbit96",
                "im Rabbit96!!",
                "19940715",
                "life is one time");
        Member saveMember = memberJpaRepository.save(member);
        //when
        mockMvc.perform(get(COMMON_URL+"/{id}",saveMember.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.memberEmail").value("jipdol2@gmail.com"))
                .andExpect(jsonPath("$.password").value("1234"))
                .andDo(print());

    }

    @Test
    @DisplayName("회원 프로필 이미지 업로드 : /api/member/profileImage 200 status code 리턴")
    void uploadProfileImageTest() throws Exception{

        Member member = createMember(
                "jipdol2@gmail.com",
                "1234",
                "Rabbit96",
                "im Rabbit96!!",
                "19940715",
                "life is one time");
        Member saveMember = memberJpaRepository.save(member);

        String originalFilename = "testImage.jpg";
        String filePath = "src/test/resources/img/" + originalFilename;

        /**
         * MockMultiparFile
         * - String name : 서버에서 request 객체로 받을 변수이름과 동일해야됨
         * - String originalFilename : 이미지의 이름
         * - String contentType : 파일 타입(.jpg .png ...)
         * - byte[] content : FileInputStream(path)
         */
        MockMultipartFile mockImage = new MockMultipartFile("image", originalFilename, "image/jpg", new FileInputStream(filePath));

        mockMvc.perform(multipart(COMMON_URL+"/profileImage")
                        .file(mockImage)
                        .param("memberId",String.valueOf(saveMember.getId()))
                )
                .andExpect(status().isOk())
                .andDo(print());
    }

    private Member createMember(
            String email,
            String password,
            String nickname,
            String intro,
            String phoneNumber,
            String birthDay
    ) {
        Member member = Member.builder()
                .memberEmail(email)
                .password(password)
                .nickname(nickname)
                .intro(intro)
                .phoneNumber(phoneNumber)
                .birthDay(birthDay)
                .build();
        return member;
    }

    private MemberSaveRequestDTO createMemberSaveRequestDTO(
            String email,
            String password,
            String nickname,
            String intro,
            String phoneNumber,
            String birthDay
    ) {
        MemberSaveRequestDTO memberSaveRequestDTO = MemberSaveRequestDTO.builder()
                .memberEmail(email)
                .password(password)
                .nickname(nickname)
                .intro(intro)
                .phoneNumber(phoneNumber)
                .birthDay(birthDay)
                .build();
        return memberSaveRequestDTO;
    }

}