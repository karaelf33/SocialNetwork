package com.socialnetwork.post.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.socialnetwork.post.dto.PostResponseDTO;
import com.socialnetwork.post.service.SocialNetworkPostService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Objects;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class SocialNetworkPostControllerTest {

    @Mock
    private SocialNetworkPostService postService;

    @InjectMocks
    private SocialNetworkPostController postController;

    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();


    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(postController).build();
    }

    @Test
    void getPostById_shouldReturnPostResponseDTO() throws Exception {
        Long postId = 1L;
        PostResponseDTO postResponseDTO = new PostResponseDTO();
        postResponseDTO.setId(postId);
        Mockito.when(postService.getPostById(postId)).thenReturn(postResponseDTO);

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/api/posts/{id}", postId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(postId));

        Mockito.verify(postService, times(1)).getPostById(postId);

    }

    @Test
    void getPostById_whenPostDoesNotExist_shouldReturnNotFound() throws Exception {
        Long postId = 1L;
        Mockito.when(postService.getPostById(postId)).thenThrow(new ResourceNotFoundException("Post not found with id " + postId));

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/api/posts/{id}", postId))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ResourceNotFoundException))
                .andExpect(result -> assertEquals("Post not found with id " + postId,
                        Objects.requireNonNull(result.getResolvedException()).getMessage()));
        Mockito.verify(postService, times(1)).getPostById(postId);
    }

    @Test
    void createPost_shouldCreatePostAndReturnPostResponseDTO() throws Exception {
        PostResponseDTO postRequestDTO = new PostResponseDTO();
        postRequestDTO.setContent("test post content");

        PostResponseDTO postResponseDTO = new PostResponseDTO();
        postResponseDTO.setId(1L);
        postResponseDTO.setContent("test post content");

        Mockito.when(postService.createPost(ArgumentMatchers.any(PostResponseDTO.class))).thenReturn(postResponseDTO);

        mockMvc.perform(MockMvcRequestBuilders.post("/v1/api/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postRequestDTO)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", notNullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content", is("test post content")));

        verify(postService, times(1)).createPost(postRequestDTO);
    }
}