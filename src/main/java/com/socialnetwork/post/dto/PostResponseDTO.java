package com.socialnetwork.post.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostResponseDTO implements Serializable {
    private Date postDate;
    private String author;
    private String content;
    private Long viewCount;

}
