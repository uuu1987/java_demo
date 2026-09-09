package com.example.demo.post.file;

import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.post.Post;
import com.example.demo.post.PostRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service 
@Transactional(readOnly = true)

@RequiredArgsConstructor 
@Slf4j 
public class PostFileService {
    private final PostFileRepository postFileRepository;
    private final PostRepository postRepository;

    @Transactional 
    public PostFile upload(Long postId, MultipartFile file) throws IOException {
        Post post = postRepository.findById(postId).orElse(null);
        if (post == null){
            return null;
        }
        PostFile postFile = new PostFile();
        postFile.setPost(post);
        postFile.setOriginalFileName(file.getOriginalFilename());
        postFile.setContentType(file.getContentType());
        postFile.setFileSize(file.getSize());
        postFile.setFileData(file.getBytes());

        return postFileRepository.save(postFile);
    }

    public PostFile findById(Long fileId){
        return postFileRepository.findById(fileId).orElse(null);
    }

    public List<PostFile> findByPostId(Long postId){
        return postFileRepository.findByPostId(postId);
    }
    
    @Transactional 
    public void deleteById(Long fileId){
        postFileRepository.deleteById(fileId);
        log.info("삭제되었습니다 fileId={}", fileId);
        
    }

    public boolean isOwner(Long fileId, String userID){
        PostFile file = postFileRepository.findById(fileId).orElse(null);
        if (file == null || userID == null || file.getPost() == null || file.getPost().getWriter() == null){
            return false;
        }
        return file.getPost().getWriter().getUserID().equals(userID);
    }
}
