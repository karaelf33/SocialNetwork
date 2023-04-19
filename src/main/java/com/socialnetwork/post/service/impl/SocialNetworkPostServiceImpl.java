package com.socialnetwork.post.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.socialnetwork.post.cache.CacheService;
import com.socialnetwork.post.dto.PostResponseDTO;
import com.socialnetwork.post.mapper.SocialNetworkPostMapper;
import com.socialnetwork.post.model.SocialNetworkPost;
import com.socialnetwork.post.repository.SocialNetworkPostRepository;
import com.socialnetwork.post.service.PostCachingUtils;
import com.socialnetwork.post.service.SocialNetworkPostService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

import static com.socialnetwork.post.utils.Constants.LAST_ITEM_OF_POST_LIST_IN_CACHE;
import static com.socialnetwork.post.utils.Constants.TOP_K_POSTS_CACHE_KEY;


@Service
public class SocialNetworkPostServiceImpl implements SocialNetworkPostService, PostCachingUtils {

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
    public PostResponseDTO getPostById(Long postId) {
        SocialNetworkPost socialNetworkPost = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with id " + postId));
        return postMapper.toDto(socialNetworkPost);
    }

    @Override
    public PostResponseDTO createPost(PostResponseDTO postDTO) {
        SocialNetworkPost post = postMapper.fromDto(postDTO);
        SocialNetworkPost save = postRepository.save(post);
        logger.info("Post with ID {} created", save.getId());
        return postMapper.toDto(save);

    }

    @Override
    public PostResponseDTO deletePostById(Long postId) {
        SocialNetworkPost postToDelete = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with id " + postId));

        postRepository.delete(postToDelete);
        logger.info("Post with ID {} deleted", postId);

        List<SocialNetworkPost> cacheTopsPost = getTopsPostFromCache();
        if (cacheTopsPost!=null && cacheTopsPost.contains(postToDelete)) {
            List<SocialNetworkPost> updatedTopPosts = postRepository.findTopXByOrderByViewCountDesc(cacheTopsPost.size());
            cacheService.replace(TOP_K_POSTS_CACHE_KEY, updatedTopPosts);
            logger.info("Top posts cache updated");
        }
        return postMapper.toDto(postToDelete);
    }


    @Override
    public PostResponseDTO updatePostContentById(Long postId, String content) {
        SocialNetworkPost post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with id " + postId));
        post.setContent(content);
        SocialNetworkPost updatedPost = postRepository.save(post);
        logger.info("Post with id: {} has been updated successfully", postId);

        return postMapper.toDto(updatedPost);
    }

    @Override
    @Cacheable(value = TOP_K_POSTS_CACHE_KEY, key = "#postId", unless = "#result.viewCount < 100")
    public PostResponseDTO updatePostViewCountById(Long postId, Long viewCount) {
        SocialNetworkPost post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post" + "id" + postId));

        post.setViewCount(post.getViewCount() + viewCount);
        postRepository.save(post);
        replaceTopPostsCacheIfNewPostHasHigerViewCount(post);
        return postMapper.toDto(post);
    }

    @Override
    public List<PostResponseDTO> getTopKPostByViewCount(Integer postNumber) {
        logger.info("Entering getTopTenPostByViewCount()");

        List<SocialNetworkPost> topTenPosts = postRepository.findTopXByOrderByViewCountDesc(postNumber);
        List<PostResponseDTO> dtoList = postMapper.toDtoList(topTenPosts);
        logger.info("Returning topTenPosts: {}", dtoList);
        return dtoList;
    }

    @Override
    public List<SocialNetworkPost> getTopsPostFromCache() {
        return new ObjectMapper().convertValue(
                cacheService.get(TOP_K_POSTS_CACHE_KEY), new TypeReference<>() {
                });
    }

    // Warm: should call this func when update any post view count
    @Override
    public void replaceTopPostsCacheIfNewPostHasHigerViewCount(SocialNetworkPost newPost) {
        List<SocialNetworkPost> topsPostFromCache = getTopsPostFromCache();
        if (topsPostFromCache.contains(newPost)) {
            topsPostFromCache.set(topsPostFromCache.indexOf(newPost), newPost);
        } else if (topsPostFromCache.get(LAST_ITEM_OF_POST_LIST_IN_CACHE).getViewCount() < newPost.getViewCount()) {
            topsPostFromCache.remove(LAST_ITEM_OF_POST_LIST_IN_CACHE);
            topsPostFromCache.add(newPost);
            topsPostFromCache.sort(Comparator.comparingLong(SocialNetworkPost::getViewCount).reversed());
        }
        cacheService.replace(TOP_K_POSTS_CACHE_KEY, topsPostFromCache);
    }

}

