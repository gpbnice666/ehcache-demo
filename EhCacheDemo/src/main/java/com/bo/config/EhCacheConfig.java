package com.bo.config;

import com.bo.pojo.BaseObject;
import com.bo.props.EhCachePros;
import org.ehcache.CacheManager;
import org.ehcache.PersistentCacheManager;
import org.ehcache.config.CacheConfiguration;
import org.ehcache.config.ResourcePools;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ExpiryPolicyBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.config.units.MemoryUnit;
import org.ehcache.expiry.ExpiryPolicy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.util.Set;

/**
 * 配置CacheManager
 * @author gpb
 * @date 2022/8/12 10:13
 */
@Configuration
@EnableCaching
public class EhCacheConfig {

    @Autowired
    private EhCachePros ehCachePros;

    @Bean
    public CacheManager ehCacheManager(){
        // 缓存名称
        Set<String> cacheNames = ehCachePros.getCacheNames();
        System.err.println(ehCachePros.getOffheap());
        // 设置内存储大小
        ResourcePools resourcePools = ResourcePoolsBuilder.newResourcePoolsBuilder()
                .heap(ehCachePros.getHeap())
                .offheap(ehCachePros.getOffheap(), MemoryUnit.MB)
                .disk(ehCachePros.getDisk(), MemoryUnit.MB)
                .build();
        // 设置生存时间
        ExpiryPolicy<Object, Object> expiryPolicy = ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofMillis(10000));
        // 设置 CacheConfiguration
        // BaseObject是一个基础类实现了序列化接口
        CacheConfiguration<String, BaseObject> cacheConfiguration = CacheConfigurationBuilder.newCacheConfigurationBuilder(String.class, BaseObject.class, resourcePools)
                .withExpiry(expiryPolicy)
                .build();
        // 设置磁盘存储的位置
        CacheManagerBuilder<PersistentCacheManager> cacheManagerBuilder = CacheManagerBuilder.newCacheManagerBuilder()
                .with(CacheManagerBuilder.persistence(ehCachePros.getDiskDir()));
        // 设置缓存名称
        for (String cacheName : cacheNames) {
            cacheManagerBuilder.withCache(cacheName,cacheConfiguration);
        }
        // 构建
        return cacheManagerBuilder.build(true);
    }

}
