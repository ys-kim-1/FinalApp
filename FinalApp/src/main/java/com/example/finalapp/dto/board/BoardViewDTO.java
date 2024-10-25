package com.example.finalapp.dto.board;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Getter @Setter
@ToString
@NoArgsConstructor
public class BoardViewDTO {
    private Long boardId;
    private String title;
    private String content;
    private LocalDate createdDate;
    private LocalDate modifiedDate;
    private String loginId;
    private Long userId;
//    BOARD_ID, TITLE, CONTENT, CREATED_DATE, MODIFIED_DATE,
//    U.LOGIN_ID, U.USER_ID
}