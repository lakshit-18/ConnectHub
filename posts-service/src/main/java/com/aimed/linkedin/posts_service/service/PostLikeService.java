package com.aimed.linkedin.posts_service.service;

import com.aimed.linkedin.posts_service.auth.UserContextHolder;
import com.aimed.linkedin.posts_service.entity.Post;
import com.aimed.linkedin.posts_service.entity.PostLike;
import com.aimed.linkedin.posts_service.event.PostCreatedEvent;
import com.aimed.linkedin.posts_service.event.PostLikedEvent;
import com.aimed.linkedin.posts_service.exception.BadRequestException;
import com.aimed.linkedin.posts_service.exception.ResourceNotFoundException;
import com.aimed.linkedin.posts_service.repository.PostLikeRepository;
import com.aimed.linkedin.posts_service.repository.PostsRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostLikeService {
    private final PostLikeRepository postLikeRepository;
    private final PostsRepository postsRepository;

    private final KafkaTemplate<Long, PostLikedEvent> kafkaTemplate;

    public void likePost(Long postId) {
        log.info("Attempting to lie the post with the id : " + postId);
        Long userId = UserContextHolder.getCurrentUserId();
        Post post = postsRepository.findById(postId).orElseThrow(
                () -> new ResourceNotFoundException("Post not found with the id : " + postId)
        );
        boolean alreadyLiked = postLikeRepository.existsByUserIdAndPostId(userId, postId);
        if (alreadyLiked) throw new BadRequestException("Cannot like the same post again.");

        PostLike postLike = new PostLike();
        postLike.setPostId(postId);
        postLike.setUserId(userId);
        postLikeRepository.save(postLike);

        PostLikedEvent postLikedEvent = PostLikedEvent.builder()
                .postId(postLike.getId())
                .creatorId(post.getUserId())
                .likedByUserId(userId)
                .build();
        kafkaTemplate.send("post-liked-topic", postId, postLikedEvent);
        log.info("Post with id : " + postId + " has been liked successfully");
    }

    @Transactional
    public void unLikePost(Long postId) {
        log.info("Attempting to lie the post with the id : " + postId);
        boolean exists = postsRepository.existsById(postId);
        if(!exists) throw new ResourceNotFoundException("Post not found with the id : " + postId);
        Long userId = UserContextHolder.getCurrentUserId();
        boolean alreadyUnLiked = !postLikeRepository.existsByUserIdAndPostId(userId, postId);
        if (alreadyUnLiked) throw new BadRequestException("Cannot unlike the post that was not liked by the user.");

        postLikeRepository.deleteByUserIdAndPostId(userId, postId);
        log.info("Post with id : " + postId + " has been unliked successfully");
    }
}
