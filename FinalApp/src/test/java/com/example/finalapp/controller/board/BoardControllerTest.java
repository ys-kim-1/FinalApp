package com.example.finalapp.controller.board;

import com.example.finalapp.dto.board.BoardListDTO;
import com.example.finalapp.dto.board.BoardUpdateDTO;
import com.example.finalapp.dto.board.BoardViewDTO;
import com.example.finalapp.dto.board.BoardWriteDTO;
import com.example.finalapp.dto.page.Criteria;
import com.example.finalapp.dto.page.Page;
import com.example.finalapp.service.board.BoardService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BoardController.class)
@ExtendWith(MockitoExtension.class)
class BoardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BoardService boardService;

    // @BeforeEach를 사용하여 매 테스트마다 MockMvc를 리셋합니다.
    @BeforeEach
    void setUp(WebApplicationContext wac) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    void testBoardList() throws Exception {
        // given: 설정 및 준비 단계
        Criteria criteria = new Criteria(1, 9);
        List<BoardListDTO> boardList = Collections.emptyList();
        int total = 0;

        doReturn(boardList).when(boardService).findAllPage(any(Criteria.class));
        doReturn(total).when(boardService).findTotal();

        // when: "/board/list" 경로로 GET 요청을 보냄
        // then: HTTP 상태 코드 200과 "board/list" 뷰를 기대
        mockMvc.perform(get("/board/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("board/list"))
                .andExpect(model().attribute("boardList", boardList))
                .andExpect(model().attribute("page", new Page(criteria, total)));
    }

    @Test
    void testBoardWriteGet() throws Exception {
        // given: 설정 및 준비 단계
        // when: "/board/write" 경로로 GET 요청을 보냄
        // then: 로그인 페이지로 리다이렉션을 기대 (로그인하지 않은 상태)
        mockMvc.perform(get("/board/write"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/user/login"));
    }

    @Test
    void testBoardWritePost() throws Exception {
        // given: 설정 및 준비 단계
        BoardWriteDTO boardWriteDTO = new BoardWriteDTO();
        boardWriteDTO.setBoardId(1L);
        boardWriteDTO.setTitle("Test Title");
        boardWriteDTO.setContent("Test Content");
        boardWriteDTO.setUserId(1L);

        MockMultipartFile file1 = new MockMultipartFile("boardFile", "file1.txt", "text/plain", "Test file content 1".getBytes(StandardCharsets.UTF_8));
        MockMultipartFile file2 = new MockMultipartFile("boardFile", "file2.txt", "text/plain", "Test file content 2".getBytes(StandardCharsets.UTF_8));

        doNothing().when(boardService).registerBoardWithFile(any(BoardWriteDTO.class), anyList());

        // when: "/board/write" 경로로 POST 요청을 보냄
        // then: /board/list로 리다이렉트와 boardId 플래시 어트리뷰트를 기대
        mockMvc.perform(multipart("/board/write")
                        .file(file1)
                        .file(file2)
                        .content("title")
                        .content("content")
                        .sessionAttr("userId", 1L))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/board/list"))
                .andExpect(flash().attributeExists("boardId"));
    }

    @Test
    void testBoardView() throws Exception {
        // given: 설정 및 준비 단계
        long boardId = 1L;
        BoardViewDTO boardViewDTO = new BoardViewDTO();
        boardViewDTO.setBoardId(boardId);
        boardViewDTO.setTitle("Test Title");
        boardViewDTO.setContent("Test Content");

        doReturn(boardViewDTO).when(boardService).findById(anyLong());

        // when: "/board/view" 경로로 GET 요청을 보냄
        // then: HTTP 상태 코드 200과 "board/view" 뷰를 기대
        mockMvc.perform(get("/board/view").param("boardId", String.valueOf(boardId)))
                .andExpect(status().isOk())
                .andExpect(view().name("board/view"))
                .andExpect(model().attribute("board", boardViewDTO));
    }

    @Test
    void testBoardModifyGet() throws Exception {
        // given: 설정 및 준비 단계
        long boardId = 1L;
        BoardViewDTO boardViewDTO = new BoardViewDTO();
        boardViewDTO.setBoardId(boardId);
        boardViewDTO.setTitle("Test Title");
        boardViewDTO.setContent("Test Content");

        doReturn(boardViewDTO).when(boardService).findById(anyLong());

        // when: "/board/modify" 경로로 GET 요청을 보냄
        // then: HTTP 상태 코드 200과 "board/modify" 뷰를 기대
        mockMvc.perform(get("/board/modify").param("boardId", String.valueOf(boardId)))
                .andExpect(status().isOk())
                .andExpect(view().name("board/modify"))
                .andExpect(model().attribute("board", boardViewDTO));
    }

    @Test
    void testBoardModifyPost() throws Exception {
        // given: 설정 및 준비 단계
        BoardUpdateDTO boardUpdateDTO = new BoardUpdateDTO();
        boardUpdateDTO.setBoardId(1L);
        boardUpdateDTO.setTitle("Updated Test Title");
        boardUpdateDTO.setContent("Updated Test Content");

        MockMultipartFile file1 = new MockMultipartFile("boardFile", "file1.jpg", "text/plain", "Test file content 1".getBytes(StandardCharsets.UTF_8));
        MockMultipartFile file2 = new MockMultipartFile("boardFile", "file2.jpg", "text/plain", "Test file content 2".getBytes(StandardCharsets.UTF_8));

        doNothing().when(boardService).modifyBoard(any(BoardUpdateDTO.class), anyList());

        // when: "/board/modify" 경로로 POST 요청을 보냄
        // then: /board/view로 리다이렉트와 boardId 어트리뷰트를 기대
        mockMvc.perform(multipart("/board/modify")
                        .file(file1)
                        .file(file2)
                        .param("boardId", "1")
                        .param("title", "Updated Test Title")
                        .param("content", "Updated Test Content"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/board/view"))
                .andExpect(model().attribute("boardId", boardUpdateDTO.getBoardId()));
    }

    @Test
    void testBoardRemove() throws Exception {
        // given: 설정 및 준비 단계
        long boardId = 1L;
        doNothing().when(boardService).removeBoard(anyLong());

        // when: "/board/remove" 경로로 GET 요청을 보냄
        // then: /board/list로 리다이렉트를 기대
        mockMvc.perform(get("/board/remove").param("boardId", String.valueOf(boardId)))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/board/list"));
    }
}