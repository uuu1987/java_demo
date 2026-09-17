package com.example.demo.post.like;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.member.Member;
import com.example.demo.member.MemberService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor 
public class PostLikeController {
    private final PostLikeService postLikeService;
    private final MemberService memberService;

    @Operation(summary = "게시글 좋아요 토글", description = "게시글 좋아요를 누르거나 취소한다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "처리 성공"),
        @ApiResponse(responseCode = "401", description = "로그인 필요"),
        @ApiResponse(responseCode = "404", description = "게시글 없음")
    })
    @PostMapping("/posts/{postId}/likes")
    public ResponseEntity<?> togglePostLike(@Parameter(description = "게시글 id") @PathVariable("postId") Long postId, HttpSession session){
        String userID = (String) session.getAttribute("userID");
        Member member = memberService.findByUserID(userID);

        LikeResponse result = postLikeService.togglePostLike(postId, member);
        if (result == null){
            return ResponseEntity.status(404).body("게시글을 찾을 수 없음");
        }
        return ResponseEntity.status(200).body(result);
    }

    @Operation(summary = "댓글 좋아요 토글", description = "댓글 좋아요를 누르거나 취소한다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "처리 성공"),
        @ApiResponse(responseCode = "401", description = "로그인 필요"),
        @ApiResponse(responseCode = "404", description = "댓글 없음")
    })
    @PostMapping("/comments/{commentId}/likes")
    public ResponseEntity<?> toggleCommentLike(@Parameter(description = "댓글 id") @PathVariable("commentId") Long commentId, HttpSession session){
        String userID = (String) session.getAttribute("userID");
        Member member = memberService.findByUserID(userID);

        LikeResponse result = postLikeService.toggleCommentLike(commentId, member);
        if (result == null){
            return ResponseEntity.status(404).body("댓글을 찾을 수 없음");
        }
        return ResponseEntity.status(200).body(result);
    }
}