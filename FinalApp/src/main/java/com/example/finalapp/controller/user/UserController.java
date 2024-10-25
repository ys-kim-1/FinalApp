package com.example.finalapp.controller.user;

import com.example.finalapp.dto.user.UserDTO;
import com.example.finalapp.dto.user.UserSessionDTO;
import com.example.finalapp.service.user.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.servlet.view.RedirectView;

@Slf4j
@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/join")
    public String join() {
        return "user/join";
    }

    @GetMapping("/login")
    public String login() {
        return "user/login";
    }

    @PostMapping("/join")
    public String join(@ModelAttribute UserDTO userDTO) {
        log.info("userDTO = {}", userDTO);

        userService.registerUser(userDTO);

        return "redirect:/user/login";
    }

    @PostMapping("/login")
    public RedirectView login(@RequestParam("loginId") String loginId, @RequestParam("password") String password, HttpSession session) {
        log.info("로그인 시도: {}", loginId);
        UserSessionDTO loginInfo = userService.findLoginInfo(loginId, password);

        session.setAttribute("userId", loginInfo.getUserId());
        session.setAttribute("loginId", loginInfo.getLoginId());

        return new RedirectView("/board/list");
    }

    @GetMapping("/logout")
    public RedirectView logout(HttpSession session) {
        session.invalidate();

        return new RedirectView("/user/login");
    }
}