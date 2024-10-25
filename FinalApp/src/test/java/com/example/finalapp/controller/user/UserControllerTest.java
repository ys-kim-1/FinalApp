package com.example.finalapp.controller.user;

import com.example.finalapp.dto.user.UserDTO;
import com.example.finalapp.dto.user.UserSessionDTO;
import com.example.finalapp.service.user.UserService;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Mock
    private HttpSession session;

    // @BeforeEach를 사용하여 매 테스트마다 MockMvc를 리셋합니다.
    @BeforeEach
    void setUp(WebApplicationContext wac) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    void testJoinPage() throws Exception {
        // given: 설정 및 준비 단계
        // when: "/user/join" 경로로 GET 요청을 보냄
        // then: HTTP 상태 코드 200과 "user/join" 뷰를 기대
        mockMvc.perform(get("/user/join"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/join"));
    }

    @Test
    void testLoginPage() throws Exception {
        // given: 설정 및 준비 단계
        // when: "/user/login" 경로로 GET 요청을 보냄
        // then: HTTP 상태 코드 200과 "user/login" 뷰를 기대
        mockMvc.perform(get("/user/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/login"));
    }

    @Test
    void testRegisterUser() throws Exception {
        // given: userService의 registerUser 메서드에 대해 아무 동작도 하지 않도록 설정
        doNothing().when(userService).registerUser(any(UserDTO.class));

        // when: "/user/join" 경로로 POST 요청을 보냄 (UserDTO의 파라미터와 함께)
        // then: HTTP 상태 코드 3xx와 "/user/login"로 리다이렉션를 기대
        mockMvc.perform(post("/user/join")
                        .param("userId", "1")
                        .param("loginId", "testUser")
                        .param("password", "password")
                        .param("email", "test@example.com")
                        .param("address", "address")
                        .param("addressDetail", "addressDetail")
                        .param("zipcode", "zipcode"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/user/login"));
    }

//    @Test
//    void testLoginUser() throws Exception {
//        // given: userService의 findLoginInfo 메서드가 UserSessionDTO를 반환하도록 설정
//        UserSessionDTO userSessionDTO = new UserSessionDTO();
//        userSessionDTO.setUserId(1L);
//        userSessionDTO.setLoginId("testUser");
//
//        doReturn(userSessionDTO).when(userService).findLoginInfo(any(), any());
//
//        // when: "/user/login" 경로로 POST 요청을 보냄 (로그인 아이디와 비밀번호를 파라미터로)
//        // then: HTTP 상태 코드 3xx와 "/board/list"로 리다이렉션을 기대
//        mockMvc.perform(post("/user/login")
//                        .content("loginId")
//                        .param("password")
////                        .accept()
//                .andExpect(status().is3xxRedirection())
//                .andExpect(redirectedUrl("/board/list")));
//    }

    @Test
    void testLogoutUser() throws Exception {
        // given: 설정 및 준비 단계
        // when: "/user/logout" 경로로 GET 요청을 보냄
        // then: HTTP 상태 코드 3xx와 "/user/login"로 리다이렉션을 기대
        mockMvc.perform(get("/user/logout"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/user/login"));
    }
}