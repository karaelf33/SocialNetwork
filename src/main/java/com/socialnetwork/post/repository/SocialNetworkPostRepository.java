package com.socialnetwork.post.repository;

import com.socialnetwork.post.model.SocialNetworkPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SocialNetworkPostRepository extends JpaRepository<SocialNetworkPost, Long> {
    @Query("SELECT p FROM SocialNetworkPost p ORDER BY p.viewCount DESC")
    List<SocialNetworkPost> findTopPostsByViewCountDesc(int limit);
}
