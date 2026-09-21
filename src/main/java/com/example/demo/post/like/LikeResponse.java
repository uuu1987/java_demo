package com.example.demo.post.like;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter 
@Setter 
@AllArgsConstructor 
    public class LikeResponse {
    @Schema(description = "현재 사용자의 좋아요 여부", example = "true")
    private boolean liked;
    @Schema(description = "총 좋아요 수", example = "3")
    private long likeCount;
/* 
    public LikeResponse(boolean liked, long likeCount) {
        this.liked = liked;
        this.likeCount = likeCount;
    }   
*/

}
