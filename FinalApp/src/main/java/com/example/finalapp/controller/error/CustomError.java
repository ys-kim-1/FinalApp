package com.example.finalapp.controller.error;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CustomError implements ErrorController {
    @GetMapping("/error")
    public String error(HttpServletRequest req){
//        HTTP상태 코드를 req에게 받을 수 있다.

        Integer statusCode = (Integer) req.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        if(statusCode != null){
            if(statusCode == HttpStatus.NOT_FOUND.value()){
                return "error/404"; // 404에러 : 요청 페이지 찾을 수 없음
            }
        }

        return "error/500"; // 500 에러 : 서버 오류
    }
}