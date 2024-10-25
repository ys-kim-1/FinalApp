package com.example.finalapp.controller.board;

import com.example.finalapp.dto.board.BoardListDTO;
import com.example.finalapp.dto.board.BoardUpdateDTO;
import com.example.finalapp.dto.board.BoardViewDTO;
import com.example.finalapp.dto.board.BoardWriteDTO;
import com.example.finalapp.dto.page.Criteria;
import com.example.finalapp.dto.page.Page;
import com.example.finalapp.service.board.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/board")
@RequiredArgsConstructor
public class BoardController {
    private final BoardService boardService;


    @GetMapping("/list")
    public String boardList(Criteria criteria, Model model){
        List<BoardListDTO> boardList = boardService.findAllPage(criteria);
        int total = boardService.findTotal();
        Page page = new Page(criteria, total);

        model.addAttribute("boardList", boardList);
        model.addAttribute("page", page);
        return "board/list";
    }

    @GetMapping("/write")
    public String boardWrite(@SessionAttribute(value = "userId", required = false) Long userId){
        return userId == null ? "redirect:/user/login" : "/board/write";
    }

    @PostMapping("/write")
    public String boardWrite(BoardWriteDTO boardWriteDTO, @SessionAttribute("userId") Long userId,
                             RedirectAttributes redirectAttributes,
                             @RequestParam("boardFile")List<MultipartFile> files)  {

        boardWriteDTO.setUserId(userId);
        System.out.println("boardWriteDTO: " + boardWriteDTO);

        try {
            boardService.registerBoardWithFile(boardWriteDTO, files);
        }catch (IOException e){
            e.printStackTrace();
        }

        redirectAttributes.addFlashAttribute("boardId", boardWriteDTO.getBoardId());

        //redirectAttributes 객체로 데이터를 저장하는 방식
        //1. 쿼리스트링
        // 컨트롤러 메소드의 매개변수로 데이터를 전달할 때 사용
        // 컨트롤러에서 직접적으로 사용되는 데이터를 전달할 때 사용한다

        //2. 플래시 영역
        // 컨트롤러 메소드의 model 객체에 데이터를 저장할 때 사용
        // 최종적으로 띄워지는 화면에서 데이터를 사용할 목적으로 플래시에 저장한다

//        addFlashAttribute(키, 값) 은 리다이렉트 되는 url과 매핑된 컨트롤러 메소드의 model 객체에 데이터를 저장시킨다
//        /board/list와 매핑된 메소드의 model 객체에 데이터를 저장한다

        return "redirect:/board/list";
    }


    @GetMapping("/view")
    public String boardView(@RequestParam("boardId") Long boardId, Model model){
        System.out.println("view 컨트롤러");
        BoardViewDTO board = boardService.findById(boardId);
        model.addAttribute("board", board);

        return "board/view";
    }


    @GetMapping("/modify")
    public String boardModify(@RequestParam("boardId")Long boardId, Model model){
        BoardViewDTO board = boardService.findById(boardId);
        model.addAttribute("board", board);

        return "board/modify";
    }


    @PostMapping("/modify")
    public String boardModify(BoardUpdateDTO boardUpdateDTO,
                              @RequestParam("boardFile") List<MultipartFile> files,
                              RedirectAttributes redirectAttributes){
        try {
            boardService.modifyBoard(boardUpdateDTO, files);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //RedirectAttributes 객체는 스프링에서 지원하는 객체이다
        //리다이렉트 할 때 데이터를 들고 가는 것은 일반적으로 불가능하다
        //그나마 고려할 수 있는 방법은 쿼리 스트링을 사용하는 것이다
        //(브라우저가 리다이렉션을 받으면 해당 URL로 get요청을 하기 때문이다)
        //반환문자열에 쿼리스트링을 직접 달아도 되지만, 스프링에서는 권장하지 않는다
        //스프링에서는 RedirectAttributes 객체에 addAttribute(키, 값)을 이용하면 자동으로 쿼리스트링을 만들어준다
        redirectAttributes.addAttribute("boardId",boardUpdateDTO.getBoardId());
        return "redirect:/board/view";
    }

    @GetMapping("/remove")
    public String boardRemove(@RequestParam("boardId") Long boardId){
        boardService.removeBoard(boardId);
        return "redirect:/board/list";
    }
}
