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

@Service 
@Transactional(readOnly = true)
@RequiredArgsConstructor 
public class PostLikeService {
    private final PostLikeRepository postLikeRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    @Transactional 
    public LikeResponse togglePostLike(Long postId, Member member){
        Post post = postRepository.findById(postId).orElse(null);
        if (post == null){
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
        return new LikeResponse(liked, count);
    }

    @Transactional
    public LikeResponse toggleCommentLike(Long commentId, Member member){
        Comment comment = commentRepository.findById(commentId).orElse(null);
        if (comment == null) return null;

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
        return new LikeResponse(liked, count);
    }
}
