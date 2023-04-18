package com.socialnetwork.post.model;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "social_network_posts")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class SocialNetworkPost implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "post_date", nullable = false)
    private Date postDate;

    @Column(name = "author", nullable = false)
    private String author;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "view_count", nullable = false)
    private Long viewCount;

    public SocialNetworkPost(Date postDate, String author, String content, Long viewCount) {
        this.postDate = postDate;
        this.author = author;
        this.content = content;
        this.viewCount = viewCount;
    }
    public SocialNetworkPost(long id, String author, String content, Long viewCount) {
        this.id = id;
        this.author = author;
        this.content = content;
        this.viewCount = viewCount;
    }

    public SocialNetworkPost(String author, String content, Long viewCount) {
        this.author = author;
        this.content = content;
        this.viewCount = viewCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        SocialNetworkPost that = (SocialNetworkPost) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}