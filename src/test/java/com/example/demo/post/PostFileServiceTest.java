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
import com.example.demo.post.file.PostFile;
import com.example.demo.post.file.PostFileRepository;
import com.example.demo.post.file.PostFileService;

public class PostFileServiceTest {

    private PostFileRepository postFileRepository;
    private PostRepository postRepository;
    private PostFileService postFileService;

    @BeforeEach
    public void setUp(){
        postFileRepository = mock(PostFileRepository.class);
        postRepository = mock(PostRepository.class);
        postFileService = new PostFileService(postFileRepository, postRepository);
    }

    @Test
    public void isOwner_본인파일이면_true(){
        Member writer = new Member();
        writer.setUserID("hong");

        Post post = new Post();
        post.setWriter(writer);

        PostFile postFile = new PostFile();
        postFile.setPost(post);

        when(postFileRepository.findById(1L)).thenReturn(Optional.of(postFile));

        assertTrue(postFileService.isOwner(1L, "hong"));
    }

    @Test
    public void isOwner_다른사람파일이면_false(){
        Member writer = new Member();
        writer.setUserID("hong");

        Post post = new Post();
        post.setWriter(writer);

        PostFile postFile = new PostFile();
        postFile.setPost(post);

        when(postFileRepository.findById(1L)).thenReturn(Optional.of(postFile));

        assertFalse(postFileService.isOwner(1L, "kim"));
    }

    @Test
    public void isOwner_파일이없으면_false(){
        when(postFileRepository.findById(99L)).thenReturn(Optional.empty());

        assertFalse(postFileService.isOwner(99L, "hong"));
    }

    @Test
    public void deleteById_호출하면_repository의_deleteById가_호출된다(){
        postFileService.deleteById(7L);

        verify(postFileRepository, times(1)).deleteById(7L);
    }
}