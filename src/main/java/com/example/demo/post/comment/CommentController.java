package com.example.demo.post.comment;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.member.Member;
import com.example.demo.member.MemberService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor 
public class CommentController {
    private final CommentService commentService;
    private final MemberService memberService;

    @GetMapping("/posts/{postId}/comments")
    public List<CommentResponse> getComments(@PathVariable("postId") Long postId){
        return commentService.getCommentTree(postId);
    }

    @PostMapping("/posts/{postId}/comments")
    public ResponseEntity<?> addComment(@PathVariable("postId") Long postId, @Valid @RequestBody CommentRequest req, HttpSession session){
        String userID = (String) session.getAttribute("userID");
        Member writer = memberService.findByUserID(userID);

        Comment saved = commentService.addComment(postId, req, writer);
        if (saved == null){
            return ResponseEntity.status(404).body("게시글을 찾을 수 없음");
        }
        return ResponseEntity.status(201).body(CommentResponse.from(saved, List.of()));
    }

    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<String> deleteComment(@PathVariable("commentId") Long commentId, HttpSession session){
        String userID = (String) session.getAttribute("userID");

        Comment target = commentService.findById(commentId);
        if (target == null){
            return ResponseEntity.status(404).body("댓글을 찾을 수 없음");
        }

        if (!commentService.isOwner(commentId, userID)){
            return ResponseEntity.status(403).body("본인 댓글만 삭제 가능");
        }

        if (target.isDeleted()){
            return ResponseEntity.status(409).body("이미 삭제된 댓글입니다.");
        }

        commentService.deleteById(commentId);
        return ResponseEntity.status(200).body("삭제되었습니다.");
    }
}
