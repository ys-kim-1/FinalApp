package com.example.finalapp.dto.board;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
@NoArgsConstructor
public class BoardListDTO{
    private Long boardId;
    private String title;
    private String loginId;
    private Long fileId;
    private String name;
    private String uploadPath;
    private String uuid;
}
