package com.example.finalapp.mapper.reply;

import com.example.finalapp.dto.page.Criteria;
import com.example.finalapp.dto.reply.ReplyListDTO;
import com.example.finalapp.dto.reply.ReplyUpdateDTO;
import com.example.finalapp.dto.reply.ReplyWriteDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ReplyMapper {
    void insertReply(ReplyWriteDTO replyWriteDto);

    List<ReplyListDTO> selectList(Long boardId);

    void updateReply(ReplyUpdateDTO replyUpdateDto);

    void deleteReply(Long replyId);

    List<ReplyListDTO> selectSlice(@Param("criteria") Criteria criteria,
                                   @Param("boardId") Long boardId);
}
















