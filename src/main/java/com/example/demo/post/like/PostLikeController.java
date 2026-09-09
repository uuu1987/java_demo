package com.example.demo.post.like;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.member.Member;
import com.example.demo.member.MemberService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor 
public class PostLikeController {
    private final PostLikeService postLikeService;
    private final MemberService memberService;


    @PostMapping("/posts/{postId}/likes")
    public ResponseEntity<?> togglePostLike(@PathVariable("postId") Long postId, HttpSession session){
        String userID = (String) session.getAttribute("userID");
        Member member = memberService.findByUserID(userID);

        LikeResponse result = postLikeService.togglePostLike(postId, member);
        if (result == null){
            return ResponseEntity.status(404).body("게시글을 찾을 수 없음");
        }
        return ResponseEntity.status(200).body(result);
    }

    @PostMapping("/comments/{commentId}/likes")
    public ResponseEntity<?> toggleCommentLike(@PathVariable("commentId") Long commentId, HttpSession session){
        String userID = (String) session.getAttribute("userID");
        Member member = memberService.findByUserID(userID);

        LikeResponse result = postLikeService.toggleCommentLike(commentId, member);
        if (result == null){
            return ResponseEntity.status(404).body("댓글을 찾을 수 없음");
        }
        return ResponseEntity.status(200).body(result);
    }
}