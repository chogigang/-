package com.study.board.entity;
import lombok.Data;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
@Entity //  JPA에서 사용되는 어노테이션으로, 해당 어노테이션이 붙은 클래스는 데이터베이스의 테이블과 매핑되는 객체(Entity object)로 인식됩니다. 즉, 해당 클래스는 데이터베이스 테이블과 매핑되는 클래스로 간주되며, JPA를 사용하여 이 클래스의 인스턴스를 생성, 저장, 수정, 삭제할 수 있다.
@Data //lombok의 어노테이션으로 클래스의 getter, setter, equals, hashCode, toString 등의 메소드를 자동으로 생성해줍니다. 이를 사용하면 POJO 클래스를 만들때 코드량을 줄일 수 있으며 가독성을 높일 수 있다.
public class Board {
        @Id //JPA에서 사용되는 어노테이션으로 해당 필드가 테이블의 primary key 을 나타냅니다.
        @GeneratedValue(strategy = GenerationType.IDENTITY)//aoto는 알아서 읽어줌
        private Integer id;

        private String title;

        private String content;

        private String filename;//파일이름

        private String filepath;//파일경로


}
