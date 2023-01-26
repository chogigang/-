package com.study.board.controller;
import com.study.board.entity.Board;
import com.study.board.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.sql.SQLOutput;


@Controller
public class BoardController {


    @Autowired
    private BoardService boardService;


    @GetMapping("/board/write")//local  host:8091/board/write


    public String Boardwriteform(){

        return "boardwrite";

    }
    @PostMapping("/board/writepro")//form 태그의 url 이 일치해야함
    public String boardwritepro(Board board, Model model, MultipartFile file) throws Exception{

        boardService.write(board,file);



        model.addAttribute("message","글작성이 완료되었습니다");
        model.addAttribute("searchUrl","/board/list");
        return "message";
    }


    @GetMapping("/board/list")
    public String boardList(Model model) {

            model.addAttribute("list",boardService.boardList());

        return "boardlist";
    }
        @GetMapping("/board/view")//localhost:8091/board/view?id=1
        public String boardviwe(Model model,Integer id) {

        model.addAttribute("board",boardService.boardview(id));
        return  "boardview";

    }
@GetMapping("/board/delete")
    public String boardDelete(Integer id) {

    boardService.boardDelete(id);

    return "redirect:/board/list";
/*api 통신을 할 때는 @DeleteMapping을 사용할 수 있는데 form 태그나 URL로 직접 접근하는 경우에는 @DeleteMapping을 사용할 수 없습니다!
(hidden 타입의 input 태그를 사용하여 쓸려고 하면 쓸 수는 있는데 잘 안 씁니다!) 자바스크립트에서 ajax나 리액트 등을 사용하여 api와 직접 통신을 할 때는
GET/POST/PUT/DELETE 등 다양한  HTTP 메소드를 적용할 수 있지만 여기서는 URL에 직접 접근하기 때문에 @GetMapping을 사용하여 글을 지우도록 했다
*/
    }

    @GetMapping("/board/modify/{id}")
    public String boardModify(@PathVariable("id") Integer id,
                              Model model) {

        model.addAttribute("board",boardService.boardview(id));


        return "boardmodify";

    }

@PostMapping("/board/update/{id}")
    public String boardUpdate(@PathVariable("id")Integer id,Board board, MultipartFile file) throws Exception{


        Board boardTemp = boardService.boardview(id);
        boardTemp.setTitle(board.getTitle());
        boardTemp.setContent(board.getContent());

        boardService.write(boardTemp, file);

        return "redirect:/board/list";
/* 원래 JPA에서는 수정할 때 이렇게 덮어씌워버리는 방식을 절대 사용하면 안 된다.
JPA에는 변경 감지(Dirty Checking)이라는 기능이 있어서 트랙잭션 내에서 DB에서 불러온 엔티티(객체)에 수정이 이뤄질 경우 트랙잭션이 끝날 때
자동으로 DB에 반영되기 때문에 변경 감지 기능을 써서 수정해야합니다! 이 영상에서 변경 감지를 쓰지 않고 덮어씌워버린 이유는
변경 감지가 쉬운 개념이 아니라 이것만 다뤄도 내용이 길어지기 때문에 무작정 따라하기라는 취지에 맞지 않기 때문에 다루지 않았습니다.
 하지만 JPA 공부하실 때 꼭 학습해야 될 내용이기 때문에 JPA 변경감지, JPA merge, JPA persist 등 꼭 따로 학습해야한다!*/

    }




}


