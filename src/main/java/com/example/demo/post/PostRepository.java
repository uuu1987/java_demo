package com.example.demo.post;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PostRepository extends JpaRepository<Post, Long> {
    @Query("select p from Post p join fetch p.writer where p.title like %:keyword%")
    Page<Post> findByTitleContaining(@Param("keyword") String keyword, Pageable pageable);


    @Query("select p from Post p join fetch p.writer where p.code like %:keyword%")
    Page<Post> findByCodeContaining(@Param("keyword") String keyword, Pageable pageable);

    @Query("select p from Post p join fetch p.writer")
    Page<Post> findAllWithWriter(Pageable pageable);
} 