package com.socialnetwork.post.cache;

public interface CacheService {

    void put(String key, Object value);

    Object get(String key);

    void delete(String key);

    void replace(String key, Object value);
}
