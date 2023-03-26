package com.socialnetwork.post.service;

import com.socialnetwork.post.dto.PostResponseDTO;

import java.util.List;

public interface SocialNetworkPostService {
    PostResponseDTO createPost(PostResponseDTO postDTO);
    PostResponseDTO getPostById(Long postId);

    void deletePostById(Long postId);

    PostResponseDTO updatePostContentById(Long postId, String content);

    PostResponseDTO updatePostViewCountById(Long postId, Long viewCount);

    List<PostResponseDTO> getTopTenPostByViewCount();

}

