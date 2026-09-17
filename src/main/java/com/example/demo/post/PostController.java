package com.example.demo.post;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.member.Member;
import com.example.demo.member.MemberService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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




@RequestMapping("/posts")
@Tag(name = "게시글", description = "게시글 CRUD / 페이징 / 검색")
@RestController
@RequiredArgsConstructor 
public class PostController {
    private final PostService postService;
    private final MemberService memberService;

    private final AiSummaryService aiSummaryService;


    @Operation(summary = "게시글 목록 조회", description = "페이징/검색/정렬 조건으로 게시글 목록을 조회한다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "조회 성공")
    })
    @GetMapping
    public Page<PostResponse> posts(@Parameter(description = "페이지 번호 (0부터 시작)") @RequestParam(value="page", defaultValue =  "0") int page
    ,@Parameter(description = "페이지당 게시글 개수 (기본값 10)")  @RequestParam(value = "size", defaultValue = "10") int size
    ,@Parameter(description = "검색어 (선택 입력, 비우면 전체 조회)")  @RequestParam(value="keyword", required = false) String keyword
    ,@Parameter(description = "정렬 (기본값 : 내림차순)")  @RequestParam(value="sort", defaultValue = "desc") String sort
    ,@Parameter(description = "검색조건 (기본값 : title)")  @RequestParam(value="searchType", defaultValue = "title") String searchType
    ){
        Page<Post> posts = postService.findPosts(page, size, searchType, keyword, sort);
        return posts.map(PostResponse::from);
    }

    @Operation(summary = "게시글 단건 조회", description = "게시글 하나만 조회")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "성공"),
        @ApiResponse(responseCode = "404", description = "게시글 없음")
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> getPost(@Parameter(description = "게시글 id") @PathVariable("id") Long id) {
        Post post_id = postService.findById(id);
        if (post_id != null){
            return ResponseEntity.status(200).body(PostResponse.from(post_id));
        }else{
            return ResponseEntity.status(404).build();
        }
    }

    @Operation(summary = "게시글 저장", description = "게시글 내용 저장")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "추가완료"),
        @ApiResponse(responseCode = "400", description = "제목/내용 입력 오류"),
        @ApiResponse(responseCode = "401", description = "로그인 필요")
    })
    @PostMapping
    public ResponseEntity<String> addPost(@Valid @RequestBody PostRequest req, HttpSession session){
        String userID = (String) session.getAttribute("userID");
        Member writer = memberService.findByUserID(userID);

        Post p = new Post();
        p.setTitle(req.getTitle());
        p.setContent(req.getContent());

        postService.addPost(p, writer);
        return ResponseEntity.status(201).body("추가완료");

    }

    @Operation (summary = "게시글 삭제", description = "본인 글만 삭제 가능. 댓글/첨부파일/좋아요도 함께 삭제된다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "삭제 성공"),
        @ApiResponse(responseCode = "401", description = "로그인 필요"),
        @ApiResponse(responseCode = "403", description = "본인 글이 아님"),
        @ApiResponse(responseCode = "404", description = "게시글 없음")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePost(@Parameter(description = "게시글 id") @PathVariable("id") Long id, HttpSession session){
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

    @Operation(summary = "게시글 수정", description = "본인 글만 수정 가능.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "수정 성공"),
        @ApiResponse(responseCode = "400", description = "제목/내용 입력 오류"),
        @ApiResponse(responseCode = "401", description = "로그인 필요"),
        @ApiResponse(responseCode = "403", description = "본인 글이 아님"),
        @ApiResponse(responseCode = "404", description = "게시글 없음")
    })
    @PutMapping("/{id}")
    public ResponseEntity<String> updatePost(@Parameter(description = "게시글 id") @PathVariable("id") Long id, @Valid @RequestBody PostRequest req, HttpSession session) {
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

    @GetMapping("/{id}/summary")
    public ResponseEntity<String> getSummary(@PathVariable("id") Long id) throws Exception {
        Post post = postService.findById(id);
        if (post == null) {
            return ResponseEntity.status(404).build();
        }
        String summary = aiSummaryService.summarize(post.getContent());
        return ResponseEntity.ok(summary);
    }
}