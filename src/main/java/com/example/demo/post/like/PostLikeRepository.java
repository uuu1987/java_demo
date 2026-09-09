package com.example.demo.post.like;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {
    Optional<PostLike> findByPostIdAndMemberId(Long postId, Long memberId);
    Optional<PostLike> findByCommentIdAndMemberId(Long commentId, Long memberId);

    long countByPostId(Long postId);
    long countByCommentId(Long commentId);
    void deleteByPostId(Long postId);   
    void deleteByCommentIdIn(List<Long> commentIds);
    
}
