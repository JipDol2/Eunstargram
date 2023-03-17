package jipdol2.eunstargram.member;

import com.fasterxml.jackson.databind.ObjectMapper;
import jipdol2.eunstargram.exception.MemberNotFound;
import jipdol2.eunstargram.member.dto.request.MemberSaveRequestDTO;
import jipdol2.eunstargram.member.dto.request.MemberUpdateRequestDTO;
import jipdol2.eunstargram.member.entity.Member;
import jipdol2.eunstargram.member.entity.MemberJpaRepository;
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
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

    @Test
    @DisplayName("회원가입 : /api/member/signUp 요청시 200 status code 리턴")
    @Transactional
    void signUpTest() throws Exception {

        MemberSaveRequestDTO memberSaveRequestDTO = MemberSaveRequestDTO.builder()
                        .memberEmail("jipdol2@gmail.com")
                        .password("1234")
                        .nickname("Rabbit96")
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

        Member findByMember = memberJpaRepository.findById(1L)
                .orElseThrow(() -> new MemberNotFound());

        assertThat(findByMember.getId()).isEqualTo(findByMember.getId());
        assertThat(memberJpaRepository.count()).isEqualTo(1);
    }

    @Test
    @DisplayName("회원정보수정 : /api/member/update/{id} 요청시 200 status code 리턴")
    @Transactional
    void updateTest() throws Exception{

        //given
        Member member = createMember();
        Member saveMember = memberJpaRepository.save(member);

        Long id = saveMember.getId();
        MemberUpdateRequestDTO memberB = MemberUpdateRequestDTO.builder()
                        .password("4321")
                        .nickName("Rabbit99")
                        .build();

        //when
        mockMvc.perform(MockMvcRequestBuilders.patch(COMMON_URL + "/update/{id}", id)
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
    @Transactional
    void deleteTest() throws Exception {
        //given
        Member member = createMember();
        Member saveMember = memberJpaRepository.save(member);
        //when
        Long id=saveMember.getId();
        mockMvc.perform(MockMvcRequestBuilders.patch(COMMON_URL+"/delete/{id}",id)
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
        Member saveMember = memberJpaRepository.save(member);
        //when
        mockMvc.perform(MockMvcRequestBuilders.get(COMMON_URL+"/{id}",saveMember.getId()))
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

        mockMvc.perform(MockMvcRequestBuilders.multipart(COMMON_URL+"/profileImage")
                        .file(mockImage)
                        .param("memberId",String.valueOf(saveMember.getId()))
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