package com.example.finalapp.dto.reply;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class ReplyListDTO {
    private Long replyId;
    private String content;
    private Long boardId;
    private Long userId;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
    private String loginId;
}
