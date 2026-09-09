package com.example.demo.post;

import org.springframework.web.bind.annotation.RestController;

import com.example.demo.member.Member;
import com.example.demo.member.MemberService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

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
@RequiredArgsConstructor 
public class PostController {
    private final PostService postService;
    private final MemberService memberService;

 

    @GetMapping("/posts")
    public Page<PostResponse> posts(@RequestParam(value="page", defaultValue =  "0") int page
    , @RequestParam(value = "size", defaultValue = "10") int size
    , @RequestParam(value="keyword", required = false) String keyword
    , @RequestParam(value="sort", defaultValue = "desc") String sort
    , @RequestParam(value="searchType", defaultValue = "title") String searchType
    ){
        Page<Post> posts = postService.findPosts(page, size, searchType, keyword, sort);
        return posts.map(PostResponse::from);
    }


    @GetMapping("/posts/{id}")
    public ResponseEntity<?> getPost(@PathVariable("id") Long id) {
        Post post_id = postService.findById(id);
        if (post_id != null){

            return ResponseEntity.status(200).body(PostResponse.from(post_id));
           //return ResponseEntity.ok(post_id);     
        }else{
            return ResponseEntity.status(404).build();
        }
    }

    @PostMapping("/posts")
    public ResponseEntity<String> addPost(@Valid @RequestBody PostRequest req, HttpSession session){
        String userID = (String) session.getAttribute("userID");
        Member writer = memberService.findByUserID(userID);

        Post p = new Post();
        p.setTitle(req.getTitle());
        p.setContent(req.getContent());

        postService.addPost(p, writer);
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
    public ResponseEntity<String> updatePost(@PathVariable("id") Long id, @Valid @RequestBody PostRequest req, HttpSession session) {
        String userID = (String) session.getAttribute("userID");

        Post target = postService.findById(id);
        
        if (target == null) {
            return ResponseEntity.status(404).body("오류" + id);
        }
        if (!postService.isOwner(id, userID)){
            return ResponseEntity.status(403).body("본인 글만 수정 가능");
        }

        Post updatedInfo = new Post();
        updatedInfo.setTitle(req.getTitle());
        updatedInfo.setContent(req.getContent());

        postService.updateById(id, updatedInfo);
        return ResponseEntity.status(200).body("수정 완료 "+ id+" / 제목"+ updatedInfo.getTitle());
    }
 
}
