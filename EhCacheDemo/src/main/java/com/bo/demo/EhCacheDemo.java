package com.bo.demo;

import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ExpiryPolicyBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.config.units.MemoryUnit;
import org.junit.Test;

import java.time.Duration;

/**
 * @author gpb
 * @date 2022/8/5 13:46
 */
public class EhCacheDemo {

    @Test
    public void tes1t() throws InterruptedException {
        // 声明存储位置
        String path = "D:\\ehcache";
        // 初始化 CacheManager
        CacheManager cacheManager = CacheManagerBuilder.newCacheManagerBuilder()
                // 设置存储位置
                .with(CacheManagerBuilder.persistence(path))
                // 一个CacheManager可以管理多个Cache
                .withCache("ehcacheDemo",
                        CacheConfigurationBuilder.newCacheConfigurationBuilder(
                                String.class,
                                String.class,
                                // heap相当于设置数据在堆内存中存储的 个数 或者 大小
                                ResourcePoolsBuilder.newResourcePoolsBuilder()
                                        // 堆内内存
                                        .heap(10, MemoryUnit.MB)
                                        // 堆外内存
                                        // off-heap大小必须 大于 heap 设置的内存大小
                                        .offheap(15,MemoryUnit.MB)
                                        // 磁盘存储,记得添加true,才能正常持久化,并且序列号以及反序列化
                                        // disk大小必须 大于 off-heap 设置的内存
                                        .disk(20,MemoryUnit.MB,true))
                                // 三选一
                                // 不设置生存时间
                               // .withExpiry(ExpiryPolicy.NO_EXPIRY)
                                // 设置生存时间,从存储开始计算
                              //  .withExpiry(ExpiryPolicyBuilder.timeToIdleExpiration(Duration.ofMillis(1000)))
                                // 设置生存时间，每次获取数据后，重置生存时间
                                .withExpiry(ExpiryPolicyBuilder.timeToIdleExpiration(Duration.ofMillis(1000)))
                ).build(true);
        // 如果 CacheManagerBuilder.build(); 如果没有传参数,需要手动调用init()
        // cacheManager.init();
        // 基于 CacheManager 获取 Cache对象
        Cache<String, String> ehCache = cacheManager.getCache("ehcacheDemo", String.class, String.class);
        // 放去缓存
        ehCache.put("ehcache", "hello ehcache");
        // 取
        System.out.println(ehCache.get("ehcache"));
        Thread.sleep(500);
        System.out.println(ehCache.get("ehcache"));
        Thread.sleep(500);
        System.out.println(ehCache.get("ehcache"));
        Thread.sleep(2000);
        System.out.println(ehCache.get("ehcache"));
        // 保证数据正常持久化不丢失,记得 close()
        cacheManager.close();
    }




}
