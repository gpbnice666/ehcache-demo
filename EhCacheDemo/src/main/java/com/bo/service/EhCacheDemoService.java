package com.bo.service;

import com.bo.pojo.Item;

/**
 * @author gpb
 * @date 2022/8/12 10:36
 */
public interface EhCacheDemoService {
    String queryById(String id,String... name);

    String insert(Item item);

    void clear(String id);

    void clearAll();

    String testCaching(String id);
}
