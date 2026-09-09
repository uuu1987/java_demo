package com.example.demo.post.comment;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter 
@Setter 
@NoArgsConstructor 
public class CommentRequest {

    @NotBlank (message = "댓글 내용을 입력하세요.")
    private String content;

    private Long parentId;   // null이면 최상위 댓글, 값이 있으면 그 댓글의 대댓글

}
