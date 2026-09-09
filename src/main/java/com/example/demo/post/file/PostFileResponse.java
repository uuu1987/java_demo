package com.example.demo.post.file;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter 
@Setter 
@NoArgsConstructor 
public class PostFileResponse {
    private  Long id;
    private String originalFileName;
    private String contentType;
    private  Long fileSize;

    public static PostFileResponse from(PostFile postFile){
        PostFileResponse response = new PostFileResponse();
        response.setId(postFile.getId());
        response.setOriginalFileName(postFile.getOriginalFileName());
        response.setContentType(postFile.getContentType());
        response.setFileSize(postFile.getFileSize());
        return response;
    }

}
