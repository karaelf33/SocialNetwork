package com.socialnetwork.post.service.impl;

import com.socialnetwork.post.cache.CacheService;
import com.socialnetwork.post.dto.PostResponseDTO;
import com.socialnetwork.post.mapper.SocialNetworkPostMapper;
import com.socialnetwork.post.model.SocialNetworkPost;
import com.socialnetwork.post.repository.SocialNetworkPostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.socialnetwork.post.utils.Constants.AUTHOR;
import static com.socialnetwork.post.utils.Constants.TOP_TEN_POSTS_CACHE_KEY;
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

    @Mock
    CacheService cacheService;

    @InjectMocks
    private SocialNetworkPostServiceImpl postService;

    @BeforeEach
    void setUp() {
        postService = new SocialNetworkPostServiceImpl(postRepository, postMapper, cacheService);
    }

    @Test
    void shouldCreatePostSuccessfully() {
        // Given
        PostResponseDTO postDTO = new PostResponseDTO();
        postDTO.setAuthor(AUTHOR);
        postDTO.setContent("Content");
        SocialNetworkPost post = new SocialNetworkPost();
        post.setId(1L);
        post.setAuthor(postDTO.getAuthor());
        post.setContent(postDTO.getContent());
        when(postMapper.fromDto(postDTO)).thenReturn(post);
        when(postRepository.save(post)).thenReturn(post);
        when(postMapper.toDto(post)).thenReturn(postDTO);

        PostResponseDTO createdPostDTO = postService.createPost(postDTO);

        assertNotNull(createdPostDTO);
        assertEquals(postDTO.getAuthor(), createdPostDTO.getAuthor());
        assertEquals(postDTO.getContent(), createdPostDTO.getContent());
        verify(postRepository, times(1)).save(post);
        verify(postMapper, times(1)).toDto(post);
        verifyNoMoreInteractions(postRepository, postMapper);
    }

    @Test
    public void testGetPostByIdWhenPostExists() {
        Long postId = 1L;
        SocialNetworkPost post = new SocialNetworkPost();
        PostResponseDTO expectedDto = new PostResponseDTO();
        expectedDto.setId(postId);
        when(postRepository.findById(postId)).thenReturn(Optional.of(post));
        when(postMapper.toDto(post)).thenReturn(expectedDto);

        PostResponseDTO actualDto = postService.getPostById(postId);

        assertEquals(expectedDto, actualDto);
    }

    @Test
    public void testGetPostByIdWhenPostDoesNotExist() {
        Long postId = 1L;
        when(postRepository.findById(postId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(ResourceNotFoundException.class,
                () -> postService.getPostById(postId));
        assertEquals("Post not found with id " + postId, exception.getMessage());
    }

    @Test
    public void testUpdatePostContentByIdWhenPostExists() {
        Long postId = 1L;
        String content = "new content";
        SocialNetworkPost post = new SocialNetworkPost();
        post.setId(postId);
        post.setContent("old content");
        when(postRepository.findById(postId)).thenReturn(Optional.of(post));
        when(postRepository.save(post)).thenReturn(post);
        PostResponseDTO postDTO = new PostResponseDTO();
        postDTO.setId(postId);
        postDTO.setContent(content);
        when(postMapper.toDto(post)).thenReturn(postDTO);

        PostResponseDTO updatedPostDTO = postService.updatePostContentById(postId, content);

        assertNotNull(updatedPostDTO);
        assertEquals(postDTO, updatedPostDTO);
        verify(postRepository, times(1)).findById(postId);
        verify(postRepository, times(1)).save(post);
        verify(postMapper, times(1)).toDto(post);
        verifyNoMoreInteractions(postRepository, postMapper);
    }

    @Test
    public void testUpdatePostContentByIdWhenPostDoesNotExist() {
        Long postId = 1L;
        String content = "new content";
        when(postRepository.findById(postId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> postService.updatePostContentById(postId, content));
        verify(postRepository, times(1)).findById(postId);
        verifyNoMoreInteractions(postRepository, postMapper);
    }

    @Test
    void getTopsPostFromCache_whenCacheHasData_shouldReturnList() {
        List<SocialNetworkPost> expectedList = new ArrayList<>();
        expectedList.add(new SocialNetworkPost());
        expectedList.add(new SocialNetworkPost());
        expectedList.add(new SocialNetworkPost());
        when(cacheService.get(TOP_TEN_POSTS_CACHE_KEY)).thenReturn(expectedList);

        List<SocialNetworkPost> actualList = postService.getTopsPostFromCache();

        assertEquals(expectedList, actualList);
        verify(cacheService, Mockito.times(1))
                .get(TOP_TEN_POSTS_CACHE_KEY);
    }

    @Test
    void createPost_shouldReturnCreatedPost() {
        PostResponseDTO postDTO = new PostResponseDTO();
        postDTO.setContent("Test Content");
        postDTO.setAuthor("Test Author");
        SocialNetworkPost post = new SocialNetworkPost();
        post.setContent("Test Content");
        post.setAuthor("Test Author");
        post.setId(1L);
        when(postMapper.fromDto(postDTO)).thenReturn(post);
        when(postRepository.save(post)).thenReturn(post);
        when(postMapper.toDto(post)).thenReturn(postDTO);

        PostResponseDTO result = postService.createPost(postDTO);

        verify(postMapper, times(1)).fromDto(postDTO);
        verify(postRepository, times(1)).save(post);
        verify(postMapper, times(1)).toDto(post);
        assertNotNull(result);
        assertEquals("Test Author", result.getAuthor());
        assertEquals("Test Content", result.getContent());
    }
}