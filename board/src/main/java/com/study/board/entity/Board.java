package com.study.board.entity;
import lombok.Data;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
@Entity
@Data
public class Board {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)//aoto는 알아서 읽어줌
        private Integer id;

        private String title;

        private String content;

        private String filename;//파일이름

        private String filepath;//파일경로


}
