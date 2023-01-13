package jipdol2.eunstargram.member;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.given;

@WebMvcTest(MemberController.class)
class MemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MemberService memberService;

    private String COMMON_URL="/api/member";

    @Test
    @DisplayName("/signUp 요청시 200 status code 리턴")
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
    }

    @Test
    @DisplayName("/login 요청시 200 status + true 리턴")
    void loginTest() throws Exception {

        //given
        given(memberService.login(any())).willReturn(true);
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
}