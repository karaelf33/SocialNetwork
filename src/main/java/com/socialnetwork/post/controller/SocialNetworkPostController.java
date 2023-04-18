package com.socialnetwork.post.controller;

import com.socialnetwork.post.dto.PostResponseDTO;
import com.socialnetwork.post.service.SocialNetworkPostService;
import jakarta.validation.constraints.NotBlank;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

import static com.socialnetwork.post.utils.Constants.TOP_TEN_POSTS_CACHE_KEY;

@RestController
@RequestMapping("v1/api/posts")
public class SocialNetworkPostController {

    private final SocialNetworkPostService postService;

    public SocialNetworkPostController(SocialNetworkPostService postService) {
        this.postService = postService;
    }

    @GetMapping("/{id}")
    public PostResponseDTO getPostById(@PathVariable("id") Long postId) {
        return postService.getPostById(postId);
    }

    @PostMapping
    public PostResponseDTO createPost(@RequestBody PostResponseDTO post) {
        return postService.createPost(post);
    }

    @DeleteMapping("/{id}")
    @CacheEvict(value = TOP_TEN_POSTS_CACHE_KEY, allEntries=true)
    public PostResponseDTO deletePostById(@PathVariable("id") Long postId) {
        return postService.deletePostById(postId);
    }

    @PatchMapping("view-count/{id}/{viewCount}")
    public PostResponseDTO updatePostViewCountById(@PathVariable("id") Long postId, @PathVariable Long viewCount) {
        return postService.updatePostViewCountById(postId, viewCount);
    }

    @PatchMapping("content/{id}")
    public PostResponseDTO updatePostContentById(@PathVariable("id") Long postId,
                                                 @RequestBody @NotBlank String content) {
        return postService.updatePostContentById(postId, content);
    }

    @GetMapping("/top")
    @Cacheable(value = TOP_TEN_POSTS_CACHE_KEY)
    public List<PostResponseDTO> getTopKPostByViewCount(@RequestParam(required = false) Integer postNumber) {
        return postService.getTopKPostByViewCount(postNumber);
    }
}