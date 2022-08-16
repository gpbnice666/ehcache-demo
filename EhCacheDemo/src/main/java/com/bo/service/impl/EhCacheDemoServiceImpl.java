package com.bo.service.impl;

import com.bo.pojo.Item;
import com.bo.service.EhCacheDemoService;
import org.springframework.cache.annotation.*;
import org.springframework.stereotype.Service;

import java.util.Arrays;

/**
 * @author gpb
 * @date 2022/8/12 10:37
 */
@Service
@CacheConfig(cacheNames = "item")
public class EhCacheDemoServiceImpl implements EhCacheDemoService {

    /**
     * Cacheable 注解的参数
     * cacheNames
     *      设置缓存的名字,可以设置多个 @Cacheable(cacheNames = {"item"})
     * key
     *      设置缓存的key, key可以使用SpEL设置,比如 @Cacheable(cacheNames = {"item"},key = "#id")
     *      设置缓存的key 设置bean name到keyGenerator中 比如  @Cacheable(cacheNames = {"item"},keyGenerator = "itemKeyGenerator")
     *
     * condition
     *      在执行前,决定是否需要缓存
     *      可以在condition中编写SpEL,只要条件为true,即代表当前数据可以缓存
     *      比如 id == 123 就进缓存
     * unless
     *      执行方法之后,决定是否需要缓存
     *       unless也可以编写SpEL,调用为false时,代表数据可以缓存,如果为true,代表数据不需要缓存
     * condition和unless没有优先级之分，他的优先级在于，不缓存的优先级高于缓存
     *
     * sync
     *      解决缓存击穿问题
     *      当多个线程并发访问一个缓存的时候,当发现缓存美术缓存数据,此时会让一个线程去执行业务代码,去查询数据
     *      放到缓存中,后面的线程在查询缓存
     *
     * @param id
     * @return
     */
    @Override
    @Cacheable(cacheNames = {"item"},keyGenerator = "itemKey",unless = "#id.equals(\"123\")",sync = true)
    public String queryById(String id,String... name) {
        System.out.println("执行业务代码,查询数据库");
        try { Thread.sleep(1000); } catch (InterruptedException e) { e.printStackTrace(); }
        return id+ Arrays.toString(name);
    }

    /**
     * CachePut 用于同步数据
     * 属性和Cacheable一样
     * @param item
     * @return
     */
    @Override
    @CachePut(cacheNames = {"item"},key = "#item.id")
    public String insert(Item item) {
        System.out.println("执行业务代码,写入数据库");
        return item.getId()+item.getName();
    }

    @Override
    @CacheEvict(value = "item")
    public void clear(String id) {
        System.out.println("清空缓存");
    }

    /**
     * @CacheEvict
     * allEntries
     *      清楚所有缓存
     *  beforeInvocation
     *   如果执行清除缓存过程中，业务代码出现异常，会导致无法正常清除缓存，
     *   可以设置一个属性来保证在方法业务执行之前，就将缓存正常清除beforeInvocation设置为true
     */
    @Override
    @CacheEvict(value = "item",allEntries = true,beforeInvocation = true)
    public void clearAll() {
        int i = 1 / 0;
        System.out.println("清空所有缓存");
    }

    /**
     * @Caching 组合注解
     * @param id
     * @return
     */
    @Override
    @Caching(cacheable = {
            @Cacheable(cacheNames = "item")
    },put = {
            @CachePut(cacheNames = "item")
    })
    public String testCaching(String id) {
        return id;
    }
}
