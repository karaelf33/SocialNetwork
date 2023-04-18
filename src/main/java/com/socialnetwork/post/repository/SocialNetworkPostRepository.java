package com.socialnetwork.post.repository;

import com.socialnetwork.post.model.SocialNetworkPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SocialNetworkPostRepository extends JpaRepository<SocialNetworkPost, Long> {

    @Query("SELECT p FROM SocialNetworkPost p ORDER BY p.viewCount DESC LIMIT :x")
    List<SocialNetworkPost> findTopXByOrderByViewCountDesc(@Param("x") Integer x);

}
