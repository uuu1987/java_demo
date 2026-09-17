package com.example.demo.post.comment;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter 
@Setter 
@NoArgsConstructor 
public class CommentRequest {

    @NotBlank (message = "댓글 내용을 입력하세요.")
    @Schema(description = "댓글 내용")
    private String content;

    @Schema(description = "부모 댓글 id (null이면 최상위 댓글, 값이 있으면 대댓글)")
    private Long parentId;   // null이면 최상위 댓글, 값이 있으면 그 댓글의 대댓글

}
