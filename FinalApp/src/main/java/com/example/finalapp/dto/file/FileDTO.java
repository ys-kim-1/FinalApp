package com.example.finalapp.dto.file;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
@NoArgsConstructor
public class FileDTO {
    private Long fileId;
    private String name;
    private String uploadPath;
    private String uuid;
    private Long boardId;
    private String thumbnailPath; // 썸네일 경로 추가

}
