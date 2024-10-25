package com.example.finalapp.service.file;

import com.example.finalapp.dto.file.FileDTO;
import com.example.finalapp.mapper.file.FileMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class FileService {

    private final FileMapper fileMapper;

    public void registerFile(FileDTO fileDTO) {
        fileMapper.insertFile(fileDTO);
    }

    public void removeFile(Long boardId) {
        fileMapper.deleteFile((boardId));
    }

    public List<FileDTO> findList(Long boardId) {
        return fileMapper.selectList(boardId);
    }
}