package com.socialnetwork.post.mapper;

import com.socialnetwork.post.dto.PostResponseDTO;
import com.socialnetwork.post.model.SocialNetworkPost;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring",uses = SocialNetworkPost.class)
public interface SocialNetworkPostMapper {

    PostResponseDTO toDto(SocialNetworkPost post);
    SocialNetworkPost fromDto(PostResponseDTO dto);
    List<PostResponseDTO> toDtoList(List<SocialNetworkPost> postList);
    List<SocialNetworkPost> fromDtoList(List<PostResponseDTO> dtoList);
}
