package com.example.demo.post;


import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.member.Member;
import com.example.demo.post.comment.Comment;
import com.example.demo.post.comment.CommentRepository;
import com.example.demo.post.file.PostFileRepository;
import com.example.demo.post.like.PostLikeRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor 
@Slf4j 
public class PostService {
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final PostLikeRepository postLikeRepository;
    private final PostFileRepository postFileRepository;

    public List<Post> findAll(){
        return postRepository.findAll();
    }
    
    public Post findById(Long id)
    {
        return postRepository.findById(id).orElse(null);
    }

    @Transactional 
    public void addPost(Post p, Member writer){
        p.setWriter(writer);
        p.setRegDate(LocalDateTime.now().toString());
        postRepository.save(p);
    }

    public boolean isOwner(Long id, String userID){
        Post post = postRepository.findById(id).orElse(null);
        if (post == null || userID == null || post.getWriter() == null){
            return false;
        }
        return post.getWriter().getUserID().equals(userID);
    }

    @Transactional
    public  void deleteById(Long id){
        log.debug("deleteById 진입, id={}", id);   // 새로 추가
        log.info("게시글 삭제요청 : id={}", id);
     
        List<Comment> comments = commentRepository.findByPostId(id);
        List<Long> commentIds = comments.stream().map(Comment::getId).collect(Collectors.toList());

            if (!commentIds.isEmpty()) {
                postLikeRepository.deleteByCommentIdIn(commentIds);
            }

            postLikeRepository.deleteByPostId(id);

            postFileRepository.deleteByPostId(id);
            commentRepository.deleteByPostId(id);
            postRepository.deleteById(id);

            log.info("게시글 삭제 완료: id={}", id);   // ← 성공했을 때도 하나 남겨두면 좋음
    
    }

    @Transactional
    public boolean updateById(Long id, Post updatedPost){
        Post upPost = postRepository.findById(id).orElse(null);
    
            updatedPost.setId(upPost.getId());
            updatedPost.setWriter(upPost.getWriter());
            updatedPost.setCode(upPost.getCode());
            updatedPost.setRegDate(upPost.getRegDate());
            updatedPost.setUpdateDate(LocalDateTime.now().toString());
            postRepository.save(updatedPost);
            return true;
      
    }

    public Page<Post> findPosts(int page, int size, String searchType, String keyword, String sort){
        Sort sortop = sort.equals("asc") ? Sort.by("id").ascending() : Sort.by("id").descending();
        Pageable pageable = PageRequest.of(page, size, sortop);
        if (keyword == null || keyword.isEmpty()) {
            return postRepository.findAllWithWriter(pageable);
        }else{
            if ("code".equals(searchType)) {
                return postRepository.findByCodeContaining(keyword, pageable);
            }
            return postRepository.findByTitleContaining(keyword, pageable);
        }
    }

}
