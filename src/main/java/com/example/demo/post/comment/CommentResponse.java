package com.example.demo.post.comment;

import java.util.List;

import com.example.demo.member.MemberResponse;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter 
@Setter 
@NoArgsConstructor 
public class CommentResponse {

    private Long id;
    private String content;
    private MemberResponse writer;
    private String regDate;
    private boolean isDeleted;
    private List<CommentResponse> replies;


    public static CommentResponse from(Comment comment, List<CommentResponse> repiles){
        CommentResponse res = new CommentResponse();
        res.setId(comment.getId());
        res.setContent(comment.isDeleted() ? "삭제된 댓글입니다." :comment.getContent());
        res.setWriter(comment.getWriter() != null ? MemberResponse.from(comment.getWriter()) : null);
        res.setRegDate(comment.getRegDate() != null ? comment.getRegDate().toString() : null);
        res.setDeleted(comment.isDeleted());
        res.setReplies(repiles);
        return res;
    }

}
