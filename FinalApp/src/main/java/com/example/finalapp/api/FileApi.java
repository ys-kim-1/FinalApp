package com.example.finalapp.api;

import com.example.finalapp.dto.file.FileDTO;
import com.example.finalapp.service.file.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
//@RequestMapping("/api/files")
public class FileApi {

    private final FileService fileService;

    @Value("${file.dir}")
    private String fileDir;

    /*
    RESTful
    문서, 이미지, 데이터 등등의 자원을 이름으로 구분하여 데이터를 주고받는 스타일을 의미한다.(일종의 약속)
    이때 주고받는 데이터의 형식은 JSON, XML 등을 사용한다.

    예를 들어 댓글에 대한 정보를 DB에서 가져와야하는 상황이라면 필요한 자원이 댓글이다.
    이 자원의 이름을 복수형으로 사용하여 url에 사용한다.
    이때 무엇을 할 것인지는 HTTP Method를 이용하여 구분한다.

    예) GET : /replies/list, DELETE : /replies/{댓글번호}
    CRUD 별 HTTP Method
    - Create : 삽입(POST)
    - Read : 조회 (GET)
    - Update : 수정(PUT, PATCH)
        * PUT : 전체 수정
        * PATCH : 부분 수정
    - Delete : 삭제(DELETE)

    정리하자면 REST란 자원을 중점으로 직관적인 URL과 HTTP Method를 사용하여 웹서비스를 제공하는 것을 의미한다.
    이렇게 REST라는 규칙을 지켜서 만든 웹 서비스를 RESTful웹 서비스 라고 부른다.
     */

    @GetMapping("/v1/boards/{boardId}/files")
    public List<FileDTO> fileList(@PathVariable("boardId") Long boardId){
        return fileService.findList(boardId);
    }


    @GetMapping("/v1/files")
    public byte[] display(@RequestParam("fileName") String fileName) throws IOException {
        File file = new File(fileDir  +  fileName);
        return FileCopyUtils.copyToByteArray(file);
    }

    @GetMapping("/download")
//    HttpServletResponse와 동일하게 ResponseEntity객체는 응답을 나타내는 객체이다.
//    스프링에서 지원하는 응답객체이며 기존의 응답 객체보다 간편하게 설정할 수 있다는 장점이 있다.
    public ResponseEntity<Resource> download(String fileName) throws UnsupportedEncodingException {
//        Resource 객체는 말 그대로 자원을 나타내는 객체이다. 스프링에서 지원하는 타입이다.
//        이미지 파일이라는 리소스를 다운로드 처리하기 위해 사용하고 있으며, File 객체보다
//        많은 종류의 리소스를 다룰 수 있고 스프링과의 호환성이 좋다.
//        Resource는 인터페이스이므로 객체화를 할 때는 자식 클래스를 사용한다.
        Resource resource = new FileSystemResource(fileDir + fileName);
        HttpHeaders headers = new HttpHeaders();

        String name = resource.getFilename();

        name = name.substring(name.indexOf("_")+1);

//        Content-Disposition 헤더로 설정하여 클라이언트 브라우저가 첨부파일이라는 것을 알게 함
        headers.add("Content-Disposition", "attachment;filename="+ URLEncoder.encode(name, "UTF-8"));


        return new ResponseEntity<>(resource, headers, HttpStatus.OK);
    }
}

















