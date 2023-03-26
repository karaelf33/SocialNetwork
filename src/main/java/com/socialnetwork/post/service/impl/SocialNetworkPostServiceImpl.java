package com.socialnetwork.post.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.socialnetwork.post.cache.CacheService;
import com.socialnetwork.post.dto.PostResponseDTO;
import com.socialnetwork.post.mapper.SocialNetworkPostMapper;
import com.socialnetwork.post.model.SocialNetworkPost;
import com.socialnetwork.post.repository.SocialNetworkPostRepository;
import com.socialnetwork.post.service.SocialNetworkPostService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.socialnetwork.post.utils.Constants.TOP_TEN_POSTS_CACHE_KEY;


@Service
public class SocialNetworkPostServiceImpl implements SocialNetworkPostService {

    Logger logger = LoggerFactory.getLogger(SocialNetworkPostServiceImpl.class);

    private final SocialNetworkPostRepository postRepository;
    private final SocialNetworkPostMapper postMapper;
    private final CacheService cacheService;

    public SocialNetworkPostServiceImpl(SocialNetworkPostRepository postRepository,
                                        SocialNetworkPostMapper postMapper, CacheService cacheService) {
        this.postRepository = postRepository;
        this.postMapper = postMapper;
        this.cacheService = cacheService;
    }

    @Override
    public PostResponseDTO createPost(PostResponseDTO postDTO) {
        SocialNetworkPost post = postMapper.fromDto(postDTO);
        SocialNetworkPost save = postRepository.save(post);
        return postMapper.toDto(save);
    }

    @Override
    public PostResponseDTO getPostById(Long postId) {
        SocialNetworkPost socialNetworkPost = postRepository.findById(postId)
                .orElseThrow(ResourceNotFoundException::new);
        return postMapper.toDto(socialNetworkPost);
    }

    @Override
    public void deletePostById(Long postId) {
        SocialNetworkPost post = postRepository.findById(postId).orElseThrow(ResourceNotFoundException::new);
        postRepository.delete(post);
    }

    @Override
    public PostResponseDTO updatePostContentById(Long postId, String content) {
        Optional<SocialNetworkPost> post = postRepository.findById(postId);
        if (post.isEmpty()) {
            System.out.println("post not found");
            return null;
        }
        post.get().setContent(content);
        SocialNetworkPost updatedPost = postRepository.save(post.get());

        return postMapper.toDto(updatedPost);
    }

    @Override
    public PostResponseDTO updatePostViewCountById(Long postId, Long viewCount) {
        SocialNetworkPost post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post" + "id" + postId));

        post.setViewCount(post.getViewCount() + viewCount);

        return postMapper.toDto(post);
    }

    @Override
    @Cacheable(cacheNames = TOP_TEN_POSTS_CACHE_KEY)
    public List<PostResponseDTO> getTopTenPostByViewCount() {

        logger.info("Entering getTopTenPostByViewCount()");

        List<PostResponseDTO> topTenPosts = new ObjectMapper().convertValue(
                cacheService.getAll(TOP_TEN_POSTS_CACHE_KEY),
                new TypeReference<List<PostResponseDTO>>() {});
        if (topTenPosts.size()==0) {
            List<SocialNetworkPost> topPostsByViewCountDesc = postRepository.findTopPostsByViewCountDesc(10);
            List<PostResponseDTO> postResponseDTOS = postMapper.toDtoList(topPostsByViewCountDesc);
            topTenPosts = new ArrayList<>(postResponseDTOS);
            cacheService.put(TOP_TEN_POSTS_CACHE_KEY, topTenPosts);

        }

        return topTenPosts;
    }

}