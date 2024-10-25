package com.example.finalapp.dto.board;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString//(exclude = {"title", "content"})
@NoArgsConstructor
public class BoardWriteDTO {
    private Long boardId;
    private String title;
    private String content;
    private Long userId;
    private String createdDate;
}










