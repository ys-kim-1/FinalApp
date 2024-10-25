package com.example.finalapp.service.reply;

import com.example.finalapp.dto.page.Criteria;
import com.example.finalapp.dto.page.Slice;
import com.example.finalapp.dto.reply.ReplyListDTO;
import com.example.finalapp.dto.reply.ReplyUpdateDTO;
import com.example.finalapp.dto.reply.ReplyWriteDTO;
import com.example.finalapp.mapper.reply.ReplyMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ReplyService {
    private final ReplyMapper replyMapper;

    public void registerReply(ReplyWriteDTO replyWriteDTO){
        replyMapper.insertReply(replyWriteDTO);
    }
    public List<ReplyListDTO> findList(Long boardId){
        return replyMapper.selectList(boardId);
    }
    public void modifyReply(ReplyUpdateDTO replyUpdateDTO){
        replyMapper.updateReply(replyUpdateDTO);
    }
    public void removeReply(Long replyId){
        replyMapper.deleteReply(replyId);
    }

    public Slice<ReplyListDTO> findSlice(Criteria criteria, Long boardId){
        List<ReplyListDTO> replyList = replyMapper.selectSlice(criteria, boardId);

        boolean hasNext = replyList.size() > criteria.getAmount();

        if(hasNext){
            replyList.remove(criteria.getAmount());
        }

        return new Slice<ReplyListDTO>(hasNext, replyList);
    }
}









