package com.socialnetwork.post.service.impl;

import com.socialnetwork.post.dto.PostResponseDTO;
import com.socialnetwork.post.mapper.SocialNetworkPostMapper;
import com.socialnetwork.post.model.SocialNetworkPost;
import com.socialnetwork.post.repository.SocialNetworkPostRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SocialNetworkPostServiceImplTest {


    @Mock
    private SocialNetworkPostRepository postRepository;

    @Mock
    private SocialNetworkPostMapper postMapper;

    @InjectMocks
    private SocialNetworkPostServiceImpl postService;

    @Test
    void shouldCreatePostSuccessfully() {
        // Given
        PostResponseDTO postDTO = new PostResponseDTO();
        postDTO.setAuthor("author");
        postDTO.setContent("Content");
        SocialNetworkPost post = new SocialNetworkPost();
        post.setId(1L);
        post.setAuthor(postDTO.getAuthor());
        post.setContent(postDTO.getContent());
        when(postMapper.fromDto(postDTO)).thenReturn(post);
        when(postRepository.save(post)).thenReturn(post);
        when(postMapper.toDto(post)).thenReturn(postDTO);

        // When
        PostResponseDTO createdPostDTO = postService.createPost(postDTO);

        // Then
        assertNotNull(createdPostDTO);
        assertEquals(postDTO.getAuthor(), createdPostDTO.getAuthor());
        assertEquals(postDTO.getContent(), createdPostDTO.getContent());
        verify(postRepository, times(1)).save(post);
        verify(postMapper, times(1)).toDto(post);
        verifyNoMoreInteractions(postRepository, postMapper);
    }

    @Test
    public void testGetPostByIdWhenPostExists() {
        // Arrange
        Long postId = 1L;
        SocialNetworkPost post = new SocialNetworkPost();
        PostResponseDTO expectedDto = new PostResponseDTO();
        expectedDto.setId(postId);
        when(postRepository.findById(postId)).thenReturn(Optional.of(post));
        when(postMapper.toDto(post)).thenReturn(expectedDto);

        // Act
        PostResponseDTO actualDto = postService.getPostById(postId);

        // Assert
        assertEquals(expectedDto, actualDto);
    }

    @Test
    public void testGetPostByIdWhenPostDoesNotExist() {
        Long postId = 1L;
        when(postRepository.findById(postId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(ResourceNotFoundException.class,
                () -> postService.getPostById(postId));
        assertEquals("Post not found with id " + postId, exception.getMessage());;
    }

}