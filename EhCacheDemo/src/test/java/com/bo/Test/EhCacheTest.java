package com.bo.Test;

import com.bo.EhCacheApp;
import com.bo.pojo.Item;
import com.bo.service.EhCacheDemoService;
import net.bytebuddy.asm.Advice;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author gpb
 * @date 2022/8/12 10:45
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = EhCacheApp.class)
public class EhCacheTest {

    @Autowired
    private EhCacheDemoService ehCacheDemoService;

    @Test
    public void testEhCacheDemo(){
        new Thread(()->{
            System.out.println(ehCacheDemoService.queryById("123","王二"));
        }).start();
        System.out.println(ehCacheDemoService.queryById("123","王二"));

        ehCacheDemoService.insert(new Item(){{
            setId("123");
            setName("阿吧阿吧~~");
        }});
        System.out.println(ehCacheDemoService.queryById("123","王二"));
        ehCacheDemoService.clear("123");
        System.out.println(ehCacheDemoService.queryById("123","王二"));
        ehCacheDemoService.clearAll();
        System.out.println(ehCacheDemoService.queryById("123","王二"));
        ehCacheDemoService.testCaching("");

    }




}
