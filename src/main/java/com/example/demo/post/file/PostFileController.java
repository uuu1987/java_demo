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



@RestController 
@RequiredArgsConstructor
public class PostFileController {
    private final PostFileService postFileService;


    @PostMapping("/posts/{postID}/files")
    public ResponseEntity<?> uploadFile(@PathVariable("postID") Long postID, @RequestParam ("file") MultipartFile file) throws IOException {
       PostFile saved = postFileService.upload(postID, file);
       if (saved == null){
            return ResponseEntity.status(404).body("게시판을 찾을 수 없음");
        }
        return ResponseEntity.status(200).body(PostFileResponse.from(saved));
    
    }

    @GetMapping("/posts/{postID}/files")
    public List<PostFileResponse> ListFile(@PathVariable("postID") Long postID){
        return postFileService.findByPostId(postID).stream().map(PostFileResponse::from).toList();
    }
    
    @GetMapping("/file/{fileID}/download")
    public ResponseEntity<byte[]> downloadFile(@PathVariable("fileID") Long fileID){
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
    
    @DeleteMapping ("/file/{fileID}")
    public ResponseEntity<String> deleteFile(@PathVariable("fileID") Long fileID, HttpSession session){
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
