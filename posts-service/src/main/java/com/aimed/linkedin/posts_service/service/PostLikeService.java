package com.aimed.linkedin.posts_service.service;

import com.aimed.linkedin.posts_service.entity.PostLike;
import com.aimed.linkedin.posts_service.exception.BadRequestException;
import com.aimed.linkedin.posts_service.exception.ResourceNotFoundException;
import com.aimed.linkedin.posts_service.repository.PostLikeRepository;
import com.aimed.linkedin.posts_service.repository.PostsRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostLikeService {
    private final PostLikeRepository postLikeRepository;
    private final PostsRepository postsRepository;

    public void likePost(Long postId, Long userId) {
        log.info("Attempting to lie the post with the id : " + postId);
        boolean exists = postsRepository.existsById(postId);
        if(!exists) throw new ResourceNotFoundException("Post not found with the id : " + postId);
        boolean alreadyLiked = postLikeRepository.existsByUserIdAndPostId(userId, postId);
        if (alreadyLiked) throw new BadRequestException("Cannot like the same post again.");

        PostLike postLike = new PostLike();
        postLike.setPostId(postId);
        postLike.setUserId(userId);
        postLikeRepository.save(postLike);
        log.info("Post with id : " + postId + " has been liked successfully");
    }

    @Transactional
    public void unLikePost(Long postId, Long userId) {
        log.info("Attempting to lie the post with the id : " + postId);
        boolean exists = postsRepository.existsById(postId);
        if(!exists) throw new ResourceNotFoundException("Post not found with the id : " + postId);
        boolean alreadyUnLiked = !postLikeRepository.existsByUserIdAndPostId(userId, postId);
        if (alreadyUnLiked) throw new BadRequestException("Cannot unlike the post that was not liked by the user.");

        postLikeRepository.deleteByUserIdAndPostId(userId, postId);
        log.info("Post with id : " + postId + " has been unliked successfully");
    }
}
