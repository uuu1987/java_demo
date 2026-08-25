package com.example.demo.post;


import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class PostService {
    private final PostRepository postRepository;

    public PostService(PostRepository postRepository){
        this.postRepository = postRepository;
    }

    public List<Post> findAll(){
        return postRepository.findAll();
    }
    
    public Post findById(Long id)
    {
        return postRepository.findById(id).orElse(null);
    }

    public void addPost(Post p, String writer){
        p.setWriter(writer);
        p.setRegDate(LocalDateTime.now().toString());
        postRepository.save(p);
    }

    public boolean isOwner(Long id, String userID){
        Post post = postRepository.findById(id).orElse(null);
        if (post == null || userID == null){
            return false;
        }
        return post.getWriter().equals(userID);
    }

    public boolean deleteById(Long id){

        if (postRepository.existsById(id)){
            postRepository.deleteById(id);
            return true;
        }
       
        return false;
        
    }

    public boolean updateById(Long id, Post updatedPost){
        Post upPost = postRepository.findById(id).orElse(null);
        if (upPost != null){
            updatedPost.setId(upPost.getId());
            updatedPost.setWriter(upPost.getWriter());
            postRepository.save(updatedPost);
            return true;
        }else{
            return false;
        }
    }
}
