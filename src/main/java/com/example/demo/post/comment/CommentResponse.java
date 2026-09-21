package com.example.demo.post.comment;

import java.util.List;

import com.example.demo.member.MemberResponse;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter 
@Setter 
@NoArgsConstructor 
public class CommentResponse {

    @Schema(description = "댓글 id", example = "1")
    private Long id;
    @Schema(description = "내용 (삭제된 댓글은 '삭제된 댓글입니다.' 표시)", example = "좋은 글이네요")
    private String content;
    @Schema(description = "작성자 정보")
    private MemberResponse writer;
    @Schema(description = "등록일시")
    private String regDate;
    @Schema(description = "삭제 여부")
    private boolean isDeleted;
    @Schema(description = "대댓글 목록")
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
