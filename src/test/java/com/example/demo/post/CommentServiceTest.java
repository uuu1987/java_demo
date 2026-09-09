package com.example.demo.post;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.example.demo.member.Member;
import com.example.demo.post.comment.CommentRepository;
import com.example.demo.post.comment.CommentService;

import com.example.demo.post.comment.Comment;
public class CommentServiceTest {

    private CommentRepository commentRepository;
    private PostRepository postRepository;
    private CommentService commentService;

    

    @BeforeEach
    public void setUp() {
        commentRepository = mock(CommentRepository.class);
        postRepository = mock(PostRepository.class);
        commentService = new CommentService(commentRepository, postRepository);
    }

   
    @Test
    public void isOwner_본인댓글이면_true(){
        Member writer = new Member();
        writer.setUserID("hong");

        Comment comment = new Comment();
        comment.setWriter(writer);

        when(commentRepository.findById(1L)).thenReturn(Optional.of(comment));

        assertTrue(commentService.isOwner(1L, "hong"));
    }

    @Test
    public void isOwner_다른사람댓글이면_false(){
        Member writer = new Member();
        writer.setUserID("hong");

        Comment comment = new Comment();
        comment.setWriter(writer);

        when(commentRepository.findById(1L)).thenReturn(Optional.of(comment));
        assertFalse(commentService.isOwner(1L, "kim"));

    }

    @Test
    public void isOwner_댓글이없으면_false(){
        when(commentRepository.findById(99L)).thenReturn(Optional.empty());

        assertFalse(commentService.isOwner(99L, "hong"));

    }

    @Test
    public void deleteById_호출하면_deleted가_true로_저장된다(){
        Comment comment = new Comment();
        comment.setDeleted(false);

        when(commentRepository.findById(5L)).thenReturn(Optional.of(comment));

        commentService.deleteById(5L);

        assertTrue(comment.isDeleted());
        verify(commentRepository, times(1)).save(comment);
    }

    

}
