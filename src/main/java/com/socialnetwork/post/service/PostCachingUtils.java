package com.socialnetwork.post.service;

import com.socialnetwork.post.model.SocialNetworkPost;

import java.util.List;

public interface PostCachingUtils {

    List<SocialNetworkPost> getTopsPostFromCache();

    void updatePostViewCountInList(SocialNetworkPost post,
                                   List<SocialNetworkPost> postList);
}
