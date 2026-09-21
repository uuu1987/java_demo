package com.example.demo.post;

import com.example.demo.member.MemberResponse;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter 
@Setter 
@NoArgsConstructor 
    public class PostResponse {
    @Schema(description = "게시글 id", example = "1")
    private Long id;
    @Schema(description = "게시글 코드", example = "notice")
    private String code;
    @Schema(description = "제목", example = "제목입니다.")
    private String title;
    @Schema(description = "내용", example = "내용입니다.")
    private String content;
    @Schema(description = "작성자 정보")
    private MemberResponse writer;
    @Schema(description = "등록일시")
    private String regDate;
    @Schema(description = "수정일시")
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
