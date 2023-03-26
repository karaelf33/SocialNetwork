package com.socialnetwork.post.controller;

import com.socialnetwork.post.dto.PostResponseDTO;
import com.socialnetwork.post.service.SocialNetworkPostService;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
    public ResponseEntity<?> deletePostById(@PathVariable("id") Long postId) {
        postService.deletePostById(postId);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("view-count/{id}/{viewCount}")
    public ResponseEntity<PostResponseDTO> updatePostViewCountById(@PathVariable("id") Long postId, @PathVariable Long viewCount) {
        return ResponseEntity.ok(postService.updatePostViewCountById(postId, viewCount));
    }

    @PatchMapping("content/{id}")
    public PostResponseDTO updatePostContentById(@PathVariable("id") Long postId,
                                                 @RequestBody @NotBlank String content) {

        return postService.updatePostContentById(postId, content);
    }

    @GetMapping("/top-ten")
    public List<PostResponseDTO> getTopTenPostByViewCount() {
        return postService.getTopTenPostByViewCount();
    }
}