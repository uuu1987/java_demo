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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor 
public class CommentController {
    private final CommentService commentService;
    private final MemberService memberService;

    @Operation(summary = "댓글 트리 조회", description = "게시글의 댓글/대댓글을 트리 구조로 조회한다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "조회 성공")
    })
    @GetMapping("/posts/{postId}/comments")
    public List<CommentResponse> getComments(@Parameter(description = "게시글 id") @PathVariable("postId") Long postId){
        return commentService.getCommentTree(postId);
    }

    @Operation(summary = "댓글 작성", description = "게시글 또는 다른 댓글(대댓글)에 댓글을 작성한다.")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "작성 성공"),
        @ApiResponse(responseCode = "400", description = "댓글 내용 입력 오류"),
        @ApiResponse(responseCode = "401", description = "로그인 필요"),
        @ApiResponse(responseCode = "404", description = "게시글 없음")
    })
    @PostMapping("/posts/{postId}/comments")
    public ResponseEntity<?> addComment(@Parameter(description = "게시글 id") @PathVariable("postId") Long postId, @Valid @RequestBody CommentRequest req, HttpSession session){
        String userID = (String) session.getAttribute("userID");
        Member writer = memberService.findByUserID(userID);

        Comment saved = commentService.addComment(postId, req, writer);
        if (saved == null){
            return ResponseEntity.status(404).body("게시글을 찾을 수 없음");
        }
        return ResponseEntity.status(201).body(CommentResponse.from(saved, List.of()));
    }

    @Operation(summary = "댓글 삭제", description = "본인 댓글만 삭제 가능. 논리적 삭제로 처리된다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "삭제 성공"),
        @ApiResponse(responseCode = "401", description = "로그인 필요"),
        @ApiResponse(responseCode = "403", description = "본인 댓글이 아님"),
        @ApiResponse(responseCode = "404", description = "댓글 없음"),
        @ApiResponse(responseCode = "409", description = "이미 삭제된 댓글")
    })
    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<String> deleteComment(@Parameter(description = "댓글 id") @PathVariable("commentId") Long commentId, HttpSession session){
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