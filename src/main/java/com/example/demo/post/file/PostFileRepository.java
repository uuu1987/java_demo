package com.example.demo.post.file;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PostFileRepository extends JpaRepository<PostFile, Long> {
    List<PostFile> findByPostId(Long postId);

    void deleteByPostId(Long postId);   
}
