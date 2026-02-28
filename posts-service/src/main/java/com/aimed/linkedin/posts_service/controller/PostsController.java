package com.aimed.linkedin.posts_service.controller;

import com.aimed.linkedin.posts_service.dto.PostCreateRequestDto;
import com.aimed.linkedin.posts_service.dto.PostDto;
import com.aimed.linkedin.posts_service.entity.Post;
import com.aimed.linkedin.posts_service.service.PostsService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostsController {
    private final PostsService postsService;
    @PostMapping
    public ResponseEntity<PostDto> createPost(@RequestBody PostCreateRequestDto postDto, @RequestHeader HttpServletRequest httpServletRequest) {
        PostDto createdPost = postsService.createPost(postDto, 1L);
        return new ResponseEntity<>(createdPost, HttpStatus.CREATED);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostDto> getPost(@PathVariable Long postId) {
        PostDto post = postsService.getPostById(postId);
        return post != null ? ResponseEntity.ok(post) : ResponseEntity.notFound().build();
    }
}
