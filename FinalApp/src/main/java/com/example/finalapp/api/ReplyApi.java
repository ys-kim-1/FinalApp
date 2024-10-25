package com.example.finalapp.api;

import com.example.finalapp.dto.page.Criteria;
import com.example.finalapp.dto.page.Slice;
import com.example.finalapp.dto.reply.ReplyListDTO;
import com.example.finalapp.dto.reply.ReplyUpdateDTO;
import com.example.finalapp.dto.reply.ReplyWriteDTO;
import com.example.finalapp.service.reply.ReplyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ReplyApi {
    private final ReplyService replyService;

    @PostMapping("/v1/boards/{boardId}/reply")
    public void replyWrite(@RequestBody ReplyWriteDTO replyWriteDTO,
                           @PathVariable("boardId") Long boardId,
                           @SessionAttribute("userId") Long userId){
        replyWriteDTO.setBoardId(boardId);
        replyWriteDTO.setUserId(userId);
        log.info("replyWriteDto = " + replyWriteDTO + ", boardId = " + boardId);

        replyService.registerReply(replyWriteDTO);
    }

    @GetMapping("/v1/boards/{boardId}/replies")
    public List<ReplyListDTO> replyList(@PathVariable("boardId") Long boardId){
        return replyService.findList(boardId);
    }

    @GetMapping("/v2/boards/{boardId}/replies")
    public Slice<ReplyListDTO> replySlice(@PathVariable("boardId") Long boardId,
                                          @RequestParam("page") int page){
        Slice<ReplyListDTO> slice = replyService.findSlice(new Criteria(page, 5), boardId);
        return slice;
    }

    @PatchMapping("/v1/replies/{replyId}")
    public void modifyReply(@RequestBody ReplyUpdateDTO replyUpdateDTO,
                            @PathVariable("replyId") Long replyId){

        replyUpdateDTO.setReplyId(replyId);
        replyService.modifyReply(replyUpdateDTO);
    }

    @DeleteMapping("/v1/replies/{replyId}")
    public void removeReply(@PathVariable("replyId") Long replyId){
        replyService.removeReply(replyId);
    }
}












