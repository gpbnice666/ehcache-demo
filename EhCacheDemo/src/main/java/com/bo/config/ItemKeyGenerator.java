package com.bo.config;

import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Method;

/**
 * @author gpb
 * @date 2022/8/12 11:16
 */
@Configuration
public class ItemKeyGenerator {

    /**
     * 如果 @Bean 不指定name,默认就是就是方法的名字
     * @return
     */
    @Bean(name = "itemKey")
    public KeyGenerator itemKeyGenerator(){
        return new KeyGenerator() {
            @Override
            public Object generate(Object target, Method method, Object... params) {
                return  params[0];
            }
        };
    }

}
