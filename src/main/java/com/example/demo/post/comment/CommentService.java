package com.example.demo.post.comment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.member.Member;
import com.example.demo.post.Post;
import com.example.demo.post.PostRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service 
@Transactional(readOnly = true)
@RequiredArgsConstructor    
@Slf4j 
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    @Transactional
    public Comment addComment(Long postId, CommentRequest req, Member writer) {
        Post post = postRepository.findById(postId).orElse(null);
        if (post == null){
            return null;
        }

        Comment comment = new Comment();
        comment.setContent(req.getContent());
        comment.setWriter(writer);
        comment.setPost(post);
        comment.setRegDate(java.time.LocalDateTime.now().toString());

        if (req.getParentId() != null){
            Comment parent = commentRepository.findById(req.getParentId()).orElse(null);
            comment.setParent(parent);
        }

        return commentRepository.save(comment);        
    }

    public Comment findById(Long id){
        return commentRepository.findById(id).orElse(null);
    }

    public boolean isOwner(Long commentId, String userID){
        Comment comment = commentRepository.findById(commentId).orElse(null);
        if (comment == null || userID == null || comment.getWriter() == null){
            return false;
        }

        return comment.getWriter().getUserID().equals(userID);  
    }

    @Transactional
    public void deleteById(Long commentId){
        Comment comment = commentRepository.findById(commentId).orElse(null);

        comment.setDeleted(true);
        commentRepository.save(comment);
        log.info("삭제되었습니다. commentId={}", commentId);

    }

    public List<CommentResponse> getCommentTree(Long postId){
        List<Comment> all = commentRepository.findByPostId(postId);

        Map<Long, List<Comment>> childrenMap = new HashMap<>();
        List<Comment> topLevel = new ArrayList<>();
        for (Comment c : all){
            if (c.getParent() == null){
                topLevel.add(c);
            }else{
                childrenMap.computeIfAbsent(c.getParent().getId(), k -> new ArrayList<>()).add(c);
            }
        }

        return topLevel.stream().map(c -> buildCommentTree(c, childrenMap)).collect(Collectors.toList());
        
    }

    private CommentResponse buildCommentTree(Comment comment, Map<Long,List<Comment>> childrenMap) {
        List<Comment> children = childrenMap.getOrDefault(comment.getId(), new ArrayList<>());
        List<CommentResponse> childResponses = children.stream()
                .map(child -> buildCommentTree(child, childrenMap))  
                .collect(Collectors.toList());
        return CommentResponse.from(comment, childResponses);
    }
}
