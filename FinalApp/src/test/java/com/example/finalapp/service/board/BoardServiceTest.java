package com.example.finalapp.service.board;

import com.example.finalapp.dto.board.*;
import com.example.finalapp.dto.file.FileDTO;
import com.example.finalapp.dto.page.Criteria;
import com.example.finalapp.mapper.board.BoardMapper;
import com.example.finalapp.mapper.file.FileMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BoardServiceTest {
    @Mock
    private BoardMapper boardMapper;

    @Mock
    private FileMapper fileMapper;

    private String fileDir = "/mocked/directory"; // 가상의 파일 디렉토리 경로 설정

    @BeforeEach
    public void setup() {
        // boardService에 fileDir 경로 설정
        boardService.setFileDir(fileDir);
    }


//    @Mock
//    private File file;

    @InjectMocks
    private BoardService boardService; // 실제로 테스트할 서비스 클래스

//    @BeforeEach
//    void setup() {
//        MockitoAnnotations.openMocks(this); // Mock 객체 초기화
//    }

    @DisplayName("게시물등록")
    @Test
    void testRegisterBoard() {
        // given
        BoardWriteDTO boardWriteDTO = new BoardWriteDTO();
        doNothing().when(boardMapper).insertBoard(any(BoardWriteDTO.class));

        // when
        boardService.registerBoard(boardWriteDTO);

        // then
        verify(boardMapper, times(1)).insertBoard(any());
    }

    @DisplayName("게시물파일업로드")
    @Test
    void testRegisterBoardWithFile() throws IOException {
        // given
        BoardWriteDTO boardWriteDTO = new BoardWriteDTO();
        List<MultipartFile> files = new ArrayList<>();
        MockMultipartFile file = new MockMultipartFile("file", "test.txt", "text/plain", "some content".getBytes());
        files.add(file);

        doNothing().when(boardMapper).insertBoard(any(BoardWriteDTO.class));
        doNothing().when(fileMapper).insertFile(any(FileDTO.class));

        // when
        boardService.registerBoardWithFile(boardWriteDTO, files);

        // then
        verify(boardMapper, times(1)).insertBoard(boardWriteDTO);
        verify(fileMapper, times(1)).insertFile(any(FileDTO.class));
    }

    @DisplayName("파일저장")
    @Test
    void testSaveFile() throws IOException {
        // given
        MockMultipartFile file = new MockMultipartFile("file", "test.jpg", "text/plain", "some content".getBytes());
        String originalFilename = file.getOriginalFilename();

        // when
        FileDTO fileDTO = boardService.saveFile(file);

        // then
        assertEquals(originalFilename, fileDTO.getName());
        assertNotNull(fileDTO.getUuid());
        assertNotNull(fileDTO.getUploadPath());
    }


//    준비 단계 (given):
//    게시물 ID와 파일 목록을 설정합니다.
//    FileDTO 객체를 생성하고, UUID, 이름, 업로드 경로를 설정한 후 파일 목록에 추가합니다.
//    fileMapper와 boardMapper의 동작을 Mocking하고, 기대하는 동작을 설정합니다.
//    파일 생성 및 삭제 동작 Mocking:
//    파일이 존재하는지 확인하는 File 객체의 동작을 Mocking합니다.
//    BoardService의 getFile 메서드를 재정의하기 위해 boardService를 Spy로 감시합니다.
//    실행 단계 (when):
//    boardServiceSpy.removeBoard(boardId) 메서드를 호출하여 실제 테스트를 수행합니다.
//    검증 단계 (then):
//    fileMapper와 boardMapper의 메서드 호출이 예상대로 1번씩 이루어졌는지 검증합니다.
//    Mock된 파일이 삭제되었는지 검증합니다.

    @DisplayName("게시물 삭제")
    @Test
    void testRemoveBoard() throws IOException {
        // given
        Long boardId = 1L;
        List<FileDTO> fileList = new ArrayList<>();
        FileDTO fileDTO = new FileDTO();
        fileDTO.setUuid("test-uuid");
        fileDTO.setName("test.jpg");
        fileDTO.setUploadPath("2023/10/05");
        fileList.add(fileDTO);



        doReturn(fileList).when(fileMapper).selectList(boardId);
        doNothing().when(fileMapper).deleteFile(boardId);
        doNothing().when(boardMapper).deleteBoard(boardId);

        // Mock File creation and deletion
//        Files mockFile = mock(Files.class);
//        doReturn(true).when(Files.exists(any()));
//    //Path가 없어서 오류남 NPE
//      파일디렉터리 전달만하면됨

        Path targetPath = Paths.get(fileDir, fileDTO.getUploadPath(), fileDTO.getUuid() + "_" + fileDTO.getName());
        doReturn(true).when(Files.exists(targetPath));
        doNothing().when(Files.class);

        // Spy on BoardService to override the getFile method
        BoardService boardServiceSpy = spy(boardService);
//        doReturn(mockFile).when(boardServiceSpy).getFile(anyString(), anyString());

        // when
        boardServiceSpy.removeBoard(boardId);

        // then
        verify(fileMapper, times(1)).deleteFile(boardId);
        verify(boardMapper, times(1)).deleteBoard(boardId);
//        verify(mockFile, times(1)).delete(any());
    }

    @DisplayName("파일수정")
    @Test
    void testModifyBoard() throws IOException {
        // given
        BoardUpdateDTO boardUpdateDTO = new BoardUpdateDTO();
        boardUpdateDTO.setBoardId(1L);
        boardUpdateDTO.setTitle("Test");
        boardUpdateDTO.setContent("Test");

        List<MultipartFile> files = new ArrayList<>();
        MockMultipartFile file = new MockMultipartFile("file", "test.jpg", "text/plain", "some content".getBytes());
        files.add(file);

        doNothing().when(boardMapper).updateBoard(any(BoardUpdateDTO.class));
        doNothing().when(fileMapper).deleteFile(anyLong());
        doNothing().when(fileMapper).insertFile(any());

        // when
        boardService.modifyBoard(boardUpdateDTO, files);

        // then
        verify(boardMapper, times(1)).updateBoard(boardUpdateDTO);
        verify(fileMapper, times(1)).deleteFile(any(Long.class));
        verify(fileMapper, times(1)).insertFile(any(FileDTO.class));
    }

    @DisplayName("게시물 단건 조회")
    @Test
    void testFindById() {
        // given
        Long boardId = 1L;
        BoardViewDTO boardViewDTO = new BoardViewDTO();
        doReturn(Optional.of(boardViewDTO)).when(boardMapper).selectById(boardId);

        // when
        BoardViewDTO result = boardService.findById(boardId);

        // then
        verify(boardMapper, times(1)).selectById(boardId);
        assertNotNull(result);
    }

    @DisplayName("목록조회")
    @Test
    void testFindAll() {
        // given
        List<BoardListDTO> boardList = new ArrayList<>();
        doReturn(boardList).when(boardMapper).selectAll();

        // when
        List<BoardListDTO> result = boardService.findAll();

        // then
        verify(boardMapper, times(1)).selectAll();
        assertEquals(boardList, result);
    }

    @DisplayName("페이징처리")
    @Test
    void testFindAllPage() {
        // given
        Criteria criteria = new Criteria();
        List<BoardListDTO> boardList = new ArrayList<>();
        doReturn(boardList).when(boardMapper).selectAllPage(criteria);

        // when
        List<BoardListDTO> result = boardService.findAllPage(criteria);

        // then
        verify(boardMapper, times(1)).selectAllPage(criteria);
        assertEquals(boardList, result);
    }

    @DisplayName("게시물 전체 개수")
    @Test
    void testFindTotal() {
        // given
        int total = 10;
        doReturn(total).when(boardMapper).selectTotal();

        // when
        int result = boardService.findTotal();

        // then
        verify(boardMapper, times(1)).selectTotal();
        assertEquals(total, result);
    }
}