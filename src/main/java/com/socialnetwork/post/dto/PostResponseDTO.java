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

    private Long id;
    private Date postDate;
    private String author;
    private String content;
    private Long viewCount;

    public PostResponseDTO(Date postDate, String author, String content, Long viewCount) {
        this.postDate = postDate;
        this.author = author;
        this.content = content;
        this.viewCount = viewCount;
    }
}
