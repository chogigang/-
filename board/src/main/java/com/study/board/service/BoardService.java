package com.study.board.service;

import com.study.board.entity.Board;
import com.study.board.repository.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.io.File;
import java.util.List;
import java.util.UUID;

@Service                /*스프링에서 사용하는 어노테이션으로, 이 어노테이션을 사용하는 클래스는 서비스 계층에서 사용되는 클래스로 간주됩니다.
                         서비스 계층은 비즈니스 로직을 담당하며, 컨트롤러와 데이터베이스 사이에 위치합니다. 서비스 계층에서는 컨트롤러에서 전달받은 데이터를 가공하여 데이터베이스에 저장하거나,
                            데이터베이스에서 조회한 데이터를 가공하여 컨트롤러에 전달합니다.*/
public class BoardService {
   @Autowired //new를 써야하지만, 스프링부트가 알아서 읽어와서 주입을해준다.  이 어노테이션은 스프링이 제공하는 의존성 주입(Dependency Injection) 기능을 사용할 때 사용됩니다.
    private BoardRepository boardRepository;

   //글작성 처리
   public void write(Board board, MultipartFile file) throws Exception{
       //우리의 프로젝트경로를 담아주게 된다 - 저장할 경로를 지정
       String projectPath = System.getProperty("user.dir") + "\\board\\src\\main\\resources\\static\\files";//앞 board를 안치면 경로를 찾질 못해서 그냥 board를 추가해서 강제로 했다.

       UUID uuid = UUID.randomUUID();//랜덤식별자_원래파일이름 = 저장될 파일이름 지정

       String fileName = uuid + "_" + file.getOriginalFilename();//랜덤식별자_원래파일이름 = 저장될 파일이름 지정

       File saveFile = new File(projectPath, fileName); //File을 생성할건데, 이름은 "name" 으로할거고, projectPath 라는 경로에 담긴다는 뜻

       file.transferTo(saveFile);

       board.setFilename(fileName);//디비에 파일 넣기
       board.setFilepath("board/sec/main/resources/static/files/" + fileName);//원래  board.setFilepath ("/files/" + fileName) 만 해도 경로를 찾지만 난 안된다. 그냥 하드코딩 해버렷다

       boardRepository.save(board); //파일 저장
   }

    //게시글 리스트 처리

    public Page<Board> boardList(Pageable pageable) { //findAll : 테스트보드라는 클래스가 담긴 List를 반환하는것을 확인할수있다

        return boardRepository.findAll(pageable);
    }

    //게시글리스트처리
public Page<Board> boardSearchList(String searchKeyword, Pageable pageable) { //findAll : 테스트보드라는 클래스가 담긴 List를 반환하는것을 확인할수있다

       return boardRepository.findByTitleContaining(searchKeyword, pageable);
}


//특정 게시물 불러오기
    public Board boardview(Integer id) {

        return boardRepository.findById(id).get(); //어떤게시글을 불러올지 지정을해주어야한다 (Integer값으로)
    }
    //특정 게시물 삭제
    public void boardDelete(Integer id) { /*id값 1번을 넣어주면 1번을 삭제한다*/

        boardRepository.deleteById(id);


    }
}
