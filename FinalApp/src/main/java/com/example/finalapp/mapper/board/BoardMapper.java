package com.example.finalapp.mapper.board;

import com.example.finalapp.dto.board.BoardListDTO;
import com.example.finalapp.dto.board.BoardUpdateDTO;
import com.example.finalapp.dto.board.BoardViewDTO;
import com.example.finalapp.dto.board.BoardWriteDTO;
import com.example.finalapp.dto.page.Criteria;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Optional;

@Mapper
public interface BoardMapper {
    void insertBoard(BoardWriteDTO boardWriteDTO);

    void updateBoard(BoardUpdateDTO boardUpdateDTO);

    Optional<BoardViewDTO> selectById(Long boardId);

    List<BoardListDTO> selectAll();

    int selectTotal();

    List<BoardListDTO> selectAllPage(Criteria criteria);

    void deleteBoard(Long boardId);

}
