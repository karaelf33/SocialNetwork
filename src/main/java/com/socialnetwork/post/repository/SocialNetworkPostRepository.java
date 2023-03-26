package com.socialnetwork.post.repository;

import com.socialnetwork.post.model.SocialNetworkPost;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SocialNetworkPostRepository extends JpaRepository<SocialNetworkPost, Long> {

    //TODO find more generic method.10 should variable
    List<SocialNetworkPost> findTop10ByOrderByViewCountDesc();

}
