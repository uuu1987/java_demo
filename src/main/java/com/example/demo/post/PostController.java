package com.example.demo.post;

import org.springframework.web.bind.annotation.RestController;

import com.example.demo.member.Member;

import jakarta.servlet.http.HttpSession;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;




@RestController
public class PostController {
    private final PostService postService;

    public PostController(PostService postService){
        this.postService = postService;
    }

    @GetMapping("/posts")
    public Page<Post> posts(@RequestParam(value="page", defaultValue =  "0") int page
    , @RequestParam(value = "size", defaultValue = "10") int size
    , @RequestParam(value="keyword", required = false) String keyword
    , @RequestParam(value="sort", defaultValue = "desc") String sort
    ){
        return postService.findPosts(page, size, keyword, sort);
    }


    @GetMapping("/posts/{id}")
    public ResponseEntity<Post> getPost(@PathVariable("id") Long id) {
        Post post_id = postService.findById(id);
        if (post_id != null){
           return ResponseEntity.ok(post_id);     
        }else{
            return ResponseEntity.status(404).build();
        }
    }

    @PostMapping("/posts")
    public ResponseEntity<String> addPost(@RequestBody Post p, HttpSession session){
        Member userID = (Member) session.getAttribute("userID");
        postService.addPost(p, userID);
        return ResponseEntity.status(201).body("추가완료");
    }
    
    @DeleteMapping("/posts/{id}")
    public ResponseEntity<String> deletePost(@PathVariable("id") Long id, HttpSession session){
        String userID = (String) session.getAttribute("userID");

        Post target = postService.findById(id);
        if (target == null){
            return ResponseEntity.status(404).body("오류"+id);
        }

        if (!postService.isOwner(id, userID)){
            return ResponseEntity.status(403).body("본인 글만 삭제 가능");
        }

        postService.deleteById(id);
        return ResponseEntity.status(200).body("삭제되었습니다.");

    }

    @PutMapping("/posts/{id}")
    public ResponseEntity<String> updatePost(@PathVariable("id") Long id, @RequestBody Post updatedInfo, HttpSession session) {
        String userID = (String) session.getAttribute("userID");

        Post target = postService.findById(id);
        
        if (target == null) {
            return ResponseEntity.status(404).body("오류" + id);
        }
        if (!postService.isOwner(id, userID)){
            return ResponseEntity.status(403).body("본인 글만 수정 가능");
        }

        postService.updateById(id, updatedInfo);
        return ResponseEntity.status(200).body("수정 완료 "+ id+" / 제목"+ updatedInfo.getTitle());
    }
 
}
