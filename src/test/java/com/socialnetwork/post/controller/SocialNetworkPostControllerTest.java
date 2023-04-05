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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.testcontainers.shaded.com.fasterxml.jackson.core.type.TypeReference;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static org.springframework.http.RequestEntity.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

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

        mockMvc.perform(get("/v1/api/posts/{id}", postId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(postId));

        Mockito.verify(postService, times(1)).getPostById(postId);

    }

    @Test
    void getPostById_whenPostDoesNotExist_shouldReturnNotFound() throws Exception {
        Long postId = 1L;
        Mockito.when(postService.getPostById(postId)).thenThrow(new ResourceNotFoundException("Post not found with id " + postId));

        mockMvc.perform(get("/v1/api/posts/{id}", postId))
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

    @Test
    void getTopTenPostByViewCount_returnListOfPosts() throws Exception {
        List<PostResponseDTO> expectedPosts = Arrays.asList(new PostResponseDTO(), new PostResponseDTO(), new PostResponseDTO());
        when(postService.getTopTenPostByViewCount()).thenReturn(expectedPosts);

        mockMvc.perform(get("/v1/api/posts/top-ten")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());

        verify(postService, times(1)).getTopTenPostByViewCount();
    }

    @Test
    void deletePostWhenIfPostExist_and_return_DeletedPost() throws Exception {
        PostResponseDTO deletePost = new PostResponseDTO();
        deletePost.setId(1L);

        when(postService.deletePostById(deletePost.getId())).thenReturn(deletePost);

        mockMvc.perform(MockMvcRequestBuilders.delete("/v1/api/posts/{id}", deletePost.getId()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", notNullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.author", is(deletePost.getAuthor())));

        verify(postService, times(1)).deletePostById(deletePost.getId());

    }

    @Test
    void deletePostWhenIfNotExist_and_throwException() throws Exception {
        long postId = 1L;
        when(postService.deletePostById(postId)).thenThrow(new ResourceNotFoundException("Post not found with id " + postId));


        mockMvc.perform(MockMvcRequestBuilders.delete("/v1/api/posts/{id}", postId))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ResourceNotFoundException))
                .andExpect(result -> assertEquals("Post not found with id " + postId,
                        Objects.requireNonNull(result.getResolvedException()).getMessage()));

        verify(postService, times(1)).deletePostById(postId);

    }

    @Test
    void updatePostViewCountById_returnPostResponseDTO_whenViewCountUpdated_Successfully() throws Exception {

        long postId = 1L;
        long viewCount = 100L;
        PostResponseDTO t = new PostResponseDTO();
        t.setId(postId);
        when(postService.updatePostViewCountById(postId, viewCount)).thenReturn(t);

        mockMvc.perform(MockMvcRequestBuilders.patch("/v1/api/posts/view-count/{id}/{viewCount}"
                        , postId, viewCount))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", notNullValue()));
        verify(postService, times(1)).updatePostViewCountById(postId, viewCount);

    }

    @Test
    void updatePostViewCountById_throw_whenPostIsNotExist() throws Exception {

        long postId = 1L;
        long viewCount = 100L;
        when(postService.updatePostViewCountById(postId, viewCount))
                .thenThrow(new ResourceNotFoundException("Post not found with id " + postId));

        mockMvc.perform(MockMvcRequestBuilders.patch("/v1/api/posts/view-count/{id}/{viewCount}"
                        , postId, viewCount))
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ResourceNotFoundException))
                .andExpect(result -> assertEquals("Post not found with id " + postId,
                        Objects.requireNonNull(result.getResolvedException()).getMessage()));
        verify(postService, times(1)).updatePostViewCountById(postId, viewCount);

    }

}