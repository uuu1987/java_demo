package com.example.demo.post;


import com.example.demo.member.Member;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table (name = "T_POST")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;
    private String title;
    private String content;
    @ManyToOne
    private Member writer;
    private String RegDate;


    public Post(){}

    public Long getId(){
        return id;
    }

    public void setId(Long id){
        this.id = id;
    }

    public String getTitle(){
        return title;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public String getContent(){
        return content;
    }

    public void setContent(String content){
        this.content = content;
    }

    public Member getWriter(){
        return writer;
    }

    public void setWriter(Member writer){
        this.writer = writer;
    }


    public String getRegDate(){
        return RegDate;
    }

    public void setRegDate(String RegDate){
        this.RegDate = RegDate;
    }
    

}
