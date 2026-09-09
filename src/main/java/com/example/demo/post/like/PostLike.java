package com.example.demo.post.like;

import com.example.demo.member.Member;
import com.example.demo.post.Post;
import com.example.demo.post.comment.Comment;

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
@Table (name = "T_POST_LIKE")

@Getter 
@Setter 
@NoArgsConstructor 
public class PostLike {
    @Id 
    @GeneratedValue (strategy = GenerationType.IDENTITY)

    private Long id;
    @ManyToOne 
    private Post post;
    @ManyToOne 
    private Member member;
    @ManyToOne 
    private Comment comment;

}
