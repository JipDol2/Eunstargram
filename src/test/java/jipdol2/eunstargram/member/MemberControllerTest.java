package jipdol2.eunstargram.member;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.MockMvcPrint;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest
class MemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MemberService memberService;

    @Test
    @DisplayName("/signUp 요청시 200 status code 리턴")
    void signUpTest() throws Exception {
        //expected
        mockMvc.perform(MockMvcRequestBuilders.post("/api/member/signUp")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"userId\" : \"gch03915\"," +
                                "\"password\" : \"1234\"," +
                                "\"nickName\" : \"Rabbit96\"," +
                                "\"phoneNumber\" : \"010-1111-2222\"," +
                                "\"birthDay\" : \"20220107\"," +
                                "\"intro\" : \"AA\"," +
                                "\"imagePath\" : \"location/1234\"}")
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }
}