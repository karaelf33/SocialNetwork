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
import java.util.Optional;

import static com.socialnetwork.post.utils.Constants.TOP_TEN_POSTS_CACHE_KEY;


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
    public PostResponseDTO createPost(PostResponseDTO postDTO) {
        SocialNetworkPost post = postMapper.fromDto(postDTO);
        SocialNetworkPost save = postRepository.save(post);
        return postMapper.toDto(save);
    }

    @Override
    public PostResponseDTO getPostById(Long postId) {
        SocialNetworkPost socialNetworkPost = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with id " + postId));
        return postMapper.toDto(socialNetworkPost);
    }

    @Override
    public void deletePostById(Long postId) {
        SocialNetworkPost postToDelete = postRepository.findById(postId).orElseThrow(ResourceNotFoundException::new);
        postRepository.delete(postToDelete);
        logger.info("Post with ID {} deleted", postId);

        List<SocialNetworkPost> cacheTopsPost = getTopsPostFromCache();
        if (cacheTopsPost.contains(postToDelete)) {
            List<SocialNetworkPost> updatedTopPosts = postRepository.findTop10ByOrderByViewCountDesc();
            cacheService.replace(TOP_TEN_POSTS_CACHE_KEY, updatedTopPosts);
            logger.info("Top posts cache updated");
        }
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
        postRepository.save(post);

        List<SocialNetworkPost> cacheTopsPost = getTopsPostFromCache();
        updatePostViewCountInList(post, cacheTopsPost);
        replaceNewPostIfViewCountBiggerThenInCache(post, cacheTopsPost);

        return postMapper.toDto(post);
    }

    private void replaceNewPostIfViewCountBiggerThenInCache(SocialNetworkPost post, List<SocialNetworkPost> cacheTopsPost) {
        if (!cacheTopsPost.contains(post) && cacheTopsPost.get(9).getViewCount() < post.getViewCount()) {
            cacheTopsPost.remove(9);
            cacheTopsPost.add(post);
            cacheTopsPost.sort(Comparator.comparingLong(SocialNetworkPost::getViewCount).reversed());
            cacheService.replace(TOP_TEN_POSTS_CACHE_KEY, cacheTopsPost);
        }
    }

    @Override
    @Cacheable(value = TOP_TEN_POSTS_CACHE_KEY, key = "#root.method.name", condition = "false")
    public List<PostResponseDTO> getTopTenPostByViewCount() {
        logger.info("Entering getTopTenPostByViewCount()");
        List<SocialNetworkPost> topTenPosts = getTopsPostFromCache();

        if (topTenPosts==null || topTenPosts.isEmpty()) {
            topTenPosts = postRepository.findTop10ByOrderByViewCountDesc();
            cacheService.put(TOP_TEN_POSTS_CACHE_KEY, topTenPosts);
        }
        return postMapper.toDtoList(topTenPosts);
    }


    @Override
    public List<SocialNetworkPost> getTopsPostFromCache() {
        return new ObjectMapper().convertValue(
                cacheService.get(TOP_TEN_POSTS_CACHE_KEY), new TypeReference<>() {
                });
    }

    @Override
    public void updatePostViewCountInList(SocialNetworkPost post,
                                          List<SocialNetworkPost> postList) {
        for (SocialNetworkPost post2 : postList) {
            if (post2.getId().equals(post.getId())) {
                post2.setViewCount(post.getViewCount());
                break;
            }
        }
        cacheService.replace(TOP_TEN_POSTS_CACHE_KEY, postList);

    }
}

// TODO : unit/Integration test +generic response +properly caching
//  Log mechanism + README.md documentation+Exception mechanism
//  Add spring actuator +clean code
//  add Controller advice

