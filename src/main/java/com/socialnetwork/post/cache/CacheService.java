package com.socialnetwork.post.cache;

import java.util.List;

public interface CacheService {

    void put(String key, Object value);

    Object get(String key);

    void delete(String key);

    void replace(String key, Object value);
}
