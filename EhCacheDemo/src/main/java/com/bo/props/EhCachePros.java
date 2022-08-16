package com.bo.props;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * 加载yaml的配置项
 * @author gpb
 * @date 2022/8/12 10:08
 */
@Component
@ConfigurationProperties(prefix = "ehcache")
@Data
public class EhCachePros {

    /**
     * 堆内大小
     */
    private int heap;

    /**
     * 堆外存储大小
     */
    private int offheap;

    /**
     * 磁盘存储大小
     */
    private int disk;

    /**
     * 磁盘存储位置
     */
    private String diskDir;

    /**
     * 基于 CacheManager 构建多少个缓存
     */
    private Set<String> cacheNames;
}
