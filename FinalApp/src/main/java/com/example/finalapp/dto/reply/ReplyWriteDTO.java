package com.example.finalapp.dto.reply;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class ReplyWriteDTO {
    private Long replyId;
    private String content;
    private Long boardId;
    private Long userId;
}
