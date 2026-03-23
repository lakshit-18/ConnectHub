package com.aimed.linkedin.posts_service.service;

import com.aimed.linkedin.posts_service.auth.UserContextHolder;
import com.aimed.linkedin.posts_service.dto.PersonDto;
import com.aimed.linkedin.posts_service.dto.PostCreateRequestDto;
import com.aimed.linkedin.posts_service.dto.PostDto;
import com.aimed.linkedin.posts_service.entity.Post;
import com.aimed.linkedin.posts_service.event.PostCreatedEvent;
import com.aimed.linkedin.posts_service.exception.ResourceNotFoundException;
import com.aimed.linkedin.posts_service.feign.ConnectionsFeignClient;
import com.aimed.linkedin.posts_service.repository.PostsRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostsService {
    private final PostsRepository postsRepository;
    private final ModelMapper modelMapper;
    private final ConnectionsFeignClient connectionsFeignClient;

    private final KafkaTemplate<Long, PostCreatedEvent> kafkaTemplate;

    public PostDto createPost(PostCreateRequestDto postCreateRequestDto) {
        Long userId = UserContextHolder.getCurrentUserId();
        Post post = modelMapper.map(postCreateRequestDto, Post.class);
        post.setUserId(userId);
        Post savedPost = postsRepository.save(post);

        PostCreatedEvent postCreatedEvent = PostCreatedEvent.builder()
                .postId(savedPost.getId())
                .creatorId(savedPost.getUserId())
                .content(savedPost.getContent())
                .build();
        kafkaTemplate.send("post-created-topic", postCreatedEvent);
        return modelMapper.map(savedPost, PostDto.class);
    }

    public PostDto getPostById(Long postId) {
        log.debug("Retrieving post with Id : {}", postId);
        Long userId = UserContextHolder.getCurrentUserId();
        List<PersonDto> firstConnections = connectionsFeignClient.getMyFirstConnections();
        Post post = postsRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post not found with the id: " + postId));
        return modelMapper.map(post, PostDto.class);
    }

    public List<PostDto> getAllPostsOfUser(Long userId) {
        List<Post> posts = postsRepository.findByUserId(userId);
        return posts.stream()
                .map((element) -> modelMapper.map(element, PostDto.class))
                .collect(Collectors.toList());

    }
}
