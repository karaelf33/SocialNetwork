package com.socialnetwork.post.controller.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.socialnetwork.post.model.SocialNetworkPost;
import com.socialnetwork.post.repository.SocialNetworkPostRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class SocialNetworkPostControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SocialNetworkPostRepository postRepository;

    @Before
    public void setUp() {
        postRepository.deleteAll();
    }

    @Test
    public void deletePostById_shouldReturnNoContent() throws Exception {
        // create a post to be deleted
        SocialNetworkPost post = new SocialNetworkPost();
        post.setContent("test post");
        post.setViewCount(0L);
        post = postRepository.save(post);

        mockMvc.perform(MockMvcRequestBuilders.delete("/v1/api/posts/{id}", post.getId()))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        Optional<SocialNetworkPost> deletedPost = postRepository.findById(post.getId());
        Assert.assertFalse(deletedPost.isPresent());
    }

    @Test
    public void deletePostById_whenPostDoesNotExist_shouldReturnNotFound() throws Exception {
        Long postId = 1L;

        mockMvc.perform(MockMvcRequestBuilders.delete("/v1/api/posts/{id}", postId))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }
}
