package com.study.board.controller;
import com.study.board.entity.Board;
import com.study.board.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.sql.SQLOutput;


@Controller  //이 어노테이션은 컨트롤러 클래스를 스프링 MVC에서 사용할 수 있도록 설정하는 어노테이션입니다.
public class BoardController {


    @Autowired  //이 어노테이션은 스프링이 제공하는 의존성 주입(Dependency Injection) 기능을 사용할 때 사용됩니다.
    private BoardService boardService;


    @GetMapping("/board/write")//어떤 url로 접근할 것인지 정해주는 어노테이션 //localhost:8090/board/write


    public String Boardwriteform(){

        return "boardwrite";

    }
    @PostMapping("/board/writepro") //form 태그의 url 이 일치해야함 / @PostMapping 은 HTTP Method 중에서 POST 메소드를 의미합니다. 이 어노테이션을 사용하면 /board/writepro 이라는 URL로 POST 메소드로 요청이 올 때 이 메서드가 실행됩니다.
    public String boardwritepro(Board board, Model model, MultipartFile file) throws Exception{ //throws Exception 는 예외처리    이 메서드는 Board 객체, Model 객체, MultipartFile 객체를 인자로 받습니다. Board 객체는 게시글에 대한 정보를 담고 있는 객체이고, Model 객체는 뷰에서 사용할 데이터를 전달하는 역할을 합니다. MultipartFile 객체는 파일 업로드 기능을 구현할 때 사용합니다. throws Exception 은 이 메서드에서 발생할 수 있는 예외를 처리하는 구문입니다.

        boardService.write(board,file); //이 줄은 BoardService 개체의 write() 메서드를 호출하고 보드 및 파일 개체를 인수로 전달합니다. 이 메서드는 게시물과 파일을 데이터베이스나 저장소에 쓰는 일을 담당

        model.addAttribute("message","글작성이 완료되었습니다"); //
        model.addAttribute("searchUrl","/board/list"); //사용자에게 메시지를 표시하는 데 사용될 모델 객체에 메시지 속성을 추가하고 있습니다. 또한 모델 객체에 searchUrl 속성을 추가하고 있습니다.
        return "message";
    }


    @GetMapping("/board/list")  //이 어노테이션은 HTTP GET 요청을 특정 메서드에 매핑하는 스프링 어노테이션입니다.
    public String boardList(Model model,
                            @PageableDefault(page = 0,size = 10,sort = "id",direction = Sort.Direction.DESC) Pageable pageable,
                            String searchKeyword) {
            //검색기능-3
            Page<Board> list = null;

            if(searchKeyword == null) { /*searchKeyword = 검색하는 단어*/
                list = boardService.boardList (pageable); //기존의 리스트보여줌
            }else{
                list = boardService.boardSearchList(searchKeyword,pageable); //검색리스트반환
            }



        int nowPage = list.getPageable().getPageNumber() + 1;       //pageable에서 넘어온 현재페이지를 가지고올수있다 * 0부터시작하니까 +1
        int startPage = Math.max(nowPage - 4, 1);                   //매개변수로 들어온 두 값을 비교해서 큰값을 반환
        int endPage = Math.min(nowPage + 5, list.getTotalPages());  //BoardService에서 만들어준 boardList가 반환되는데, list라는 이름으로 받아서 넘기겠다는 뜻

        model.addAttribute("list", list);
        model.addAttribute("nowPage", nowPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        return "boardlist";
    }
        @GetMapping("/board/view")//localhost:8091/board/view?id=1     (get방식 파라미터)
        public String boardviwe(Model model,Integer id) {

        model.addAttribute("board",boardService.boardview(id));
        return  "boardview";

    }
@GetMapping("/board/delete")
    public String boardDelete(Integer id) { //게시물삭제하고 게시물리스트로 넘어가야하므로

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

@PostMapping("/board/update/{id}")  //이 어노테이션은 HTTP POST 요청을 특정 메서드에 매핑하는 스프링 어노테이션입니다.
    public String boardUpdate(@PathVariable("id")Integer id,Board board, MultipartFile file) throws Exception{
//기존에있던글이 담겨져서온다

        Board boardTemp = boardService.boardview(id);

        boardTemp.setTitle(board.getTitle());//기존에있던 내용을 새로운 내용으로 덮어씌운다.
        boardTemp.setContent(board.getContent()); //기존에있던 내용을 새로운 내용으로 덮어씌운다.

        boardService.write(boardTemp, file); //추가 → 수정한내용을 boardService의 write부분에 넣기

        return "redirect:/board/list";
/* 원래 JPA에서는 수정할 때 이렇게 덮어씌워버리는 방식을 절대 사용하면 안 된다.
JPA에는 변경 감지(Dirty Checking)이라는 기능이 있어서 트랙잭션 내에서 DB에서 불러온 엔티티(객체)에 수정이 이뤄질 경우 트랙잭션이 끝날 때
자동으로 DB에 반영되기 때문에 변경 감지 기능을 써서 수정해야합니다! 이 영상에서 변경 감지를 쓰지 않고 덮어씌워버린 이유는
변경 감지가 쉬운 개념이 아니라 이것만 다뤄도 내용이 길어지기 때문에 무작정 따라하기라는 취지에 맞지 않기 때문에 다루지 않았습니다.
 하지만 JPA 공부하실 때 꼭 학습해야 될 내용이기 때문에 JPA 변경감지, JPA merge, JPA persist 등 꼭 따로 학습해야한다!*/

    }




}


