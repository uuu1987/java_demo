package com.example.demo.post.comment;

import com.example.demo.member.Member;
import com.example.demo.post.Post;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity 
@Table (name = "T_COMMENT")

@Getter 
@Setter 
@NoArgsConstructor 
public class Comment {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    @ManyToOne 
    private Member writer;

    @ManyToOne
    private Post post;

    @ManyToOne 
    private Comment parent;

    private String regDate;

    private boolean isDeleted = false;



}
