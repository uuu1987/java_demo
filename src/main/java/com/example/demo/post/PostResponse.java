package com.example.demo.post;

import com.example.demo.member.MemberResponse;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter 
@Setter 
@NoArgsConstructor 
public class PostResponse {
    private Long id;
    private String code;
    private String title;
    private String content;
    private MemberResponse writer;
    private String regDate;
    private String UpdateDate;


    public static PostResponse from(Post post){
        PostResponse res = new PostResponse();
        res.setId(post.getId());
        res.setTitle(post.getTitle());
        res.setContent(post.getContent());
        res.setCode(post.getCode());
        res.setWriter(post.getWriter() != null ? MemberResponse.from(post.getWriter()) : null);
        res.setRegDate(post.getRegDate() != null ? post.getRegDate().toString() : null);
        res.setUpdateDate(post.getUpdateDate() != null ? post.getUpdateDate() : null);
        
        return res;
    }

}
