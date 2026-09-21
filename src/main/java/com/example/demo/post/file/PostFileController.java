package com.example.demo.post.file;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController 
@RequiredArgsConstructor
public class PostFileController {
    private final PostFileService postFileService;

    @Operation(summary = "파일 업로드", description = "게시글에 첨부파일을 업로드한다.")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "업로드 성공"),
        @ApiResponse(responseCode = "401", description = "로그인 필요"),
        @ApiResponse(responseCode = "404", description = "게시글 없음")
    })
    @PostMapping("/posts/{postID}/files")
    public ResponseEntity<?> uploadFile(@Parameter(description = "게시글 id") @PathVariable("postID") Long postID, @RequestParam ("file") MultipartFile file) throws IOException {
       PostFile saved = postFileService.upload(postID, file);
       if (saved == null){
            return ResponseEntity.status(404).body("게시판을 찾을 수 없음");
        }
        return ResponseEntity.status(201).body(PostFileResponse.from(saved));
    
    }

    @Operation(summary = "첨부파일 목록 조회", description = "게시글에 달린 첨부파일 목록(메타정보)을 조회한다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "조회 성공")
    })
    @GetMapping("/posts/{postID}/files")
    public List<PostFileResponse> ListFile(@Parameter(description = "게시글 id") @PathVariable("postID") Long postID){
        return postFileService.findByPostId(postID).stream().map(PostFileResponse::from).toList();
    }
    
    @Operation(summary = "첨부파일 다운로드", description = "첨부파일 실제 데이터를 다운로드한다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "다운로드 성공"),
        @ApiResponse(responseCode = "401", description = "로그인 필요"),
        @ApiResponse(responseCode = "404", description = "파일 없음")
    })
    @GetMapping("/file/{fileID}/download")
    public ResponseEntity<byte[]> downloadFile(@Parameter(description = "파일 id") @PathVariable("fileID") Long fileID){
        PostFile file = postFileService.findById(fileID);
        if (file == null){
            return ResponseEntity.status(404).body(null);
        }

        String encodedName = URLEncoder.encode(file.getOriginalFileName(), StandardCharsets.UTF_8);

        return ResponseEntity.ok()
        .header("Content-Disposition", "attachment; filename=\"" + encodedName + "\"")
        .contentType(MediaType.parseMediaType(file.getContentType()))
        .body(file.getFileData());


    }
    
    @Operation(summary = "첨부파일 삭제", description = "본인이 올린 파일만 삭제 가능.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "삭제 성공"),
        @ApiResponse(responseCode = "401", description = "로그인 필요"),
        @ApiResponse(responseCode = "403", description = "본인 파일이 아님"),
        @ApiResponse(responseCode = "404", description = "파일 없음")
    })
    @DeleteMapping ("/file/{fileID}")
    public ResponseEntity<String> deleteFile(@Parameter(description = "파일 id") @PathVariable("fileID") Long fileID, HttpSession session){
        String userID = (String) session.getAttribute("userID");
        
        PostFile file = postFileService.findById(fileID);
        if (file == null){
            return ResponseEntity.status(404).body("파일을 찾을 수 없음");
        }

        if (!postFileService.isOwner(fileID, userID)){
            return ResponseEntity.status(403).body("권한 없음");
        }

        
        postFileService.deleteById(fileID);
        return ResponseEntity.status(200).body("삭제 성공");
        
    }
}