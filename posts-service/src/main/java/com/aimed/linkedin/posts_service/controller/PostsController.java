package com.aimed.linkedin.posts_service.controller;

import com.aimed.linkedin.posts_service.dto.PostCreateRequestDto;
import com.aimed.linkedin.posts_service.dto.PostDto;
import com.aimed.linkedin.posts_service.service.PostsService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/core")
@RequiredArgsConstructor
public class PostsController {
    private final PostsService postsService;

    @PostMapping
    public ResponseEntity<PostDto> createPost(@RequestBody PostCreateRequestDto postDto, HttpServletRequest httpServletRequest) {
        PostDto createdPost = postsService.createPost(postDto, 1L);
        return new ResponseEntity<>(createdPost, HttpStatus.CREATED);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostDto> getPost(@PathVariable Long postId) {
        PostDto post = postsService.getPostById(postId);
        return post != null ? ResponseEntity.ok(post) : ResponseEntity.notFound().build();
    }

    @GetMapping("user/{userId}/allPosts")
    public ResponseEntity<List<PostDto>> getPostsByUserId (@PathVariable Long userId) {
        List<PostDto> usersPosts = postsService.getAllPostsOfUser(userId);
        return !CollectionUtils.isEmpty(usersPosts) ? ResponseEntity.ok(usersPosts) : ResponseEntity.notFound().build();
    }
}
