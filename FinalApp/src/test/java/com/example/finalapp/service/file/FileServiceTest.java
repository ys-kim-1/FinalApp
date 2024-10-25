package com.example.finalapp.service.file;

import com.example.finalapp.dto.file.FileDTO;
import com.example.finalapp.mapper.file.FileMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FileServiceTest {

    @InjectMocks
    private FileService fileService;

    @Mock
    private FileMapper fileMapper;



//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }

    @Test
    void testRegisterFile() {
        // given
        FileDTO fileDTO = new FileDTO();
        doNothing().when(fileMapper).insertFile(any(FileDTO.class));

        // when
        fileService.registerFile(fileDTO);

        // then
        verify(fileMapper, times(1)).insertFile(fileDTO);
    }

    @Test
    void testRemoveFile() {
        // given
        Long boardId = 1L;
        doNothing().when(fileMapper).deleteFile(boardId);

        // when
        fileService.removeFile(boardId);

        // then
        verify(fileMapper, times(1)).deleteFile(boardId);
    }

    @Test
    void testFindList() {
        // given
        Long boardId = 1L;
        List<FileDTO> fileList = Arrays.asList(new FileDTO(), new FileDTO());
        doReturn(fileList).when(fileMapper).selectList(boardId);

        // when
        List<FileDTO> result = fileService.findList(boardId);

        // then
        verify(fileMapper, times(1)).selectList(boardId);
        assertArrayEquals(fileList.toArray(), result.toArray());
    }
}