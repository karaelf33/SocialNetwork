package com.socialnetwork.post.controller.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.socialnetwork.post.dto.PostResponseDTO;
import com.socialnetwork.post.mapper.SocialNetworkPostMapper;
import com.socialnetwork.post.model.SocialNetworkPost;
import com.socialnetwork.post.repository.SocialNetworkPostRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.Instant;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;

import static com.socialnetwork.post.utils.Constants.AUTHOR;
import static com.socialnetwork.post.utils.Constants.CONTENT;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class SocialNetworkPostControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SocialNetworkPostRepository postRepository;

    @Autowired
    private SocialNetworkPostMapper postMapper;
    private final ObjectMapper objectMapper = new ObjectMapper();
    @BeforeEach
    public void setUp() {
        postRepository.deleteAll();
    }

    @Test
    public void deletePostById_whenPostExist_shouldReturnNoContent() throws Exception {
        SocialNetworkPost post = new SocialNetworkPost(Date.from(Instant.now()), AUTHOR, CONTENT, 2L);
        postRepository.save(post);

        mockMvc.perform(MockMvcRequestBuilders.delete("/v1/api/posts/{id}", post.getId()))
                .andExpect(MockMvcResultMatchers.status().isOk());

        Optional<SocialNetworkPost> deletedPost = postRepository.findById(post.getId());
        Assertions.assertFalse(deletedPost.isPresent());
    }

    @Test
    public void deletePostById_whenPostDoesNotExist_shouldReturnNotFound() throws Exception {
        Long postId = 1L;

        mockMvc.perform(MockMvcRequestBuilders.delete("/v1/api/posts/{id}", postId))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void returnPostResponseDTo_whenCreatePost_successfully() throws Exception {

        PostResponseDTO postResponseDTO = new PostResponseDTO(Date.from(Instant.now()), AUTHOR, CONTENT, 10L);
        SocialNetworkPost expected = this.postMapper.fromDto(postResponseDTO);

        mockMvc.perform(MockMvcRequestBuilders.post("/v1/api/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postResponseDTO)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", notNullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content", is(CONTENT)));

        SocialNetworkPost actual = postRepository.findAll().get(0);

        Assertions.assertNotNull(actual);
        assertEquals(expected.getViewCount(), actual.getViewCount());
        assertEquals(expected.getAuthor(), actual.getAuthor());
    }

    @Test
    public void returnPost_whenPostFound() throws Exception {
        SocialNetworkPost post = new SocialNetworkPost(Date.from(Instant.now()), AUTHOR, CONTENT, 2L);
        SocialNetworkPost saved = postRepository.save(post);
        mockMvc.perform(MockMvcRequestBuilders.get("/v1/api/posts/{id}", saved.getId()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(saved.getId()));

    }

    @Test
    public void returnException_whenPostNotFound() throws Exception {
        Long postId = 1L;

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/api/posts/{id}", postId))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ResourceNotFoundException))
                .andExpect(result -> assertEquals("Post not found with id " + postId,
                        Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }

}
