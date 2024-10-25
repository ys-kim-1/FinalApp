package com.example.finalapp.dto.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
@NoArgsConstructor
public class UserSessionDTO {
    private Long userId;
    private String loginId;
}
