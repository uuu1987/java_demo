package com.example.demo.post.like;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.member.Member;
import com.example.demo.post.Post;
import com.example.demo.post.PostRepository;
import com.example.demo.post.comment.Comment;
import com.example.demo.post.comment.CommentRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service 
@Transactional(readOnly = true)
@RequiredArgsConstructor 
@Slf4j
public class PostLikeService {
    private final PostLikeRepository postLikeRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    @Transactional 
    public LikeResponse togglePostLike(Long postId, Member member){
        log.debug("togglePostLike 진입, postId={}, memberId={}", postId, member.getId());

        Post post = postRepository.findById(postId).orElse(null);
        if (post == null){
            log.warn("좋아요 대상 게시글 없음: postId={}", postId);
            return null;
        }
        
        Optional<PostLike> existing = postLikeRepository.findByPostIdAndMemberId(postId, member.getId());
        if (existing.isPresent()){
            postLikeRepository.delete(existing.get());
        } else {
            PostLike newLike = new PostLike();
            newLike.setPost(post);
            newLike.setMember(member);
            postLikeRepository.save(newLike);
        }

        boolean liked = existing.isEmpty();
        long count = postLikeRepository.countByPostId(postId);
        log.info("게시글 좋아요 토글 완료: postId={}, memberId={}, liked={}, count={}", postId, member.getId(), liked, count);
        return new LikeResponse(liked, count);
    }

    @Transactional
    public LikeResponse toggleCommentLike(Long commentId, Member member){
        log.debug("toggleCommentLike 진입, commentId={}, memberId={}", commentId, member.getId());

        Comment comment = commentRepository.findById(commentId).orElse(null);
        if (comment == null) {
            log.warn("좋아요 대상 댓글 없음: commentId={}", commentId);
            return null;
        }

        Optional<PostLike> existing = postLikeRepository.findByCommentIdAndMemberId(commentId, member.getId());

        if (existing.isPresent()){
            postLikeRepository.delete(existing.get());
        }else{
            PostLike like = new PostLike();
            like.setComment(comment);
            like.setMember(member);
            postLikeRepository.save(like);
        }

        boolean liked = existing.isEmpty();
        long count = postLikeRepository.countByCommentId(commentId);
        log.info("댓글 좋아요 토글 완료: commentId={}, memberId={}, liked={}, count={}", commentId, member.getId(), liked, count);
        return new LikeResponse(liked, count);
    }
}
