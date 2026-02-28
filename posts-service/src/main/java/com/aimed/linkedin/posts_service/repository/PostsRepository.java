package com.aimed.linkedin.posts_service.repository;

import com.aimed.linkedin.posts_service.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostsRepository extends JpaRepository<Post, Long> {
}
