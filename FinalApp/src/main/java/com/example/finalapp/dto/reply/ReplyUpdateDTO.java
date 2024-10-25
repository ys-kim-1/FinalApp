package com.example.finalapp.dto.reply;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter @Getter @ToString
@NoArgsConstructor
public class ReplyUpdateDTO {
    private Long replyId;
    private String content;
}
