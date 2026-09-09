package com.example.demo.post;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.example.demo.member.Member;
import com.example.demo.post.comment.CommentRepository;
import com.example.demo.post.file.PostFileRepository;
import com.example.demo.post.like.PostLikeRepository;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import com.example.demo.post.comment.Comment;

public class PostServiceTest {

    private PostRepository postRepository;
    private PostService postService;
    private CommentRepository commentRepository;
    private PostLikeRepository postLikeRepository;
    private PostFileRepository postFileRepository;
    

    @BeforeEach
    public void setUp() {
        postRepository = mock(PostRepository.class);
        commentRepository = mock(CommentRepository.class);
        postLikeRepository = mock(PostLikeRepository.class);
        postFileRepository = mock(PostFileRepository.class);
        postService = new PostService(postRepository, commentRepository, postLikeRepository, postFileRepository);
    }

    @Test
    public void isOwner_본인글이면_true(){
        Member writer = new Member();
        writer.setUserID("hong");

        Post post = new Post();
        post.setWriter(writer);

        when(postRepository.findById(1L)).thenReturn(Optional.of(post));

        assertTrue(postService.isOwner(1L, "hong"));
    }

    @Test
    public void isOwner_다른사람글이면_false(){
        Member writer = new Member();
        writer.setUserID("hong");

        Post post = new Post();
        post.setWriter(writer);

        when(postRepository.findById(1L)).thenReturn(Optional.of(post));

        assertFalse(postService.isOwner(1L, "kim"));
    }

    @Test   
    public void isOwner_글이없으면_false(){
        when(postRepository.findById(99L)).thenReturn(Optional.empty());

        assertFalse(postService.isOwner(99L, "hong"));
    }


    @Test
    public void deleteById_댓글이있으면_댓글좋아요까지_함께_지운다(){
        Comment c1 = new Comment();
        c1.setId(10L);
        Comment c2 = new Comment();
        c2.setId(11L);

        when(postRepository.existsById(1L)).thenReturn(true);
        when(commentRepository.findByPostId(1L)).thenReturn(List.of(c1, c2));

        postService.deleteById(1L);

        verify(postLikeRepository, times(1)).deleteByCommentIdIn(List.of(10L, 11L));
        verify(postLikeRepository, times(1)).deleteByPostId(1L);
        verify(postFileRepository, times(1)).deleteByPostId(1L);
        verify(commentRepository, times(1)).deleteByPostId(1L);
        verify(postRepository, times(1)).deleteById(1L);
    }

    @Test
    public void deleteById_댓글이없으면_댓글좋아요_일괄삭제는_호출안함(){
        when(postRepository.existsById(2L)).thenReturn(true);              // 글은 있어야 하니까
        when(commentRepository.findByPostId(2L)).thenReturn(List.of());         // 댓글이 "없다" = 빈 리스트

        postService.deleteById(2L);   // postService.deleteById(2L) 호출

        verify(postLikeRepository, never()).deleteByCommentIdIn(org.mockito.ArgumentMatchers.anyList());
        verify(postLikeRepository, times(1)).deleteByPostId(2L);   // 몇 번 id로?
    }
}
