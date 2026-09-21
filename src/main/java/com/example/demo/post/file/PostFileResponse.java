package com.example.demo.post.file;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter 
@Setter 
@NoArgsConstructor 
    public class PostFileResponse {
    @Schema(description = "파일 id", example = "1")
    private Long id;
    @Schema(description = "원본 파일명", example = "report.pdf")
    private String originalFileName;
    @Schema(description = "MIME 타입", example = "application/pdf")
    private String contentType;
    @Schema(description = "파일 크기(byte)", example = "204800")
    private Long fileSize;

    public static PostFileResponse from(PostFile postFile){
        PostFileResponse response = new PostFileResponse();
        response.setId(postFile.getId());
        response.setOriginalFileName(postFile.getOriginalFileName());
        response.setContentType(postFile.getContentType());
        response.setFileSize(postFile.getFileSize());
        return response;
    }

}
