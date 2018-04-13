package com.am.socket.service;

import com.am.socket.Application;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.TimeUnit;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class RedisServiceTest {
    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    @Test
    public void stringTest(){
        ValueOperations<String,Object> valueOperations = redisTemplate.opsForValue();
        valueOperations.set("hello", "redis", 10, TimeUnit.SECONDS);
        String captcha = (String) valueOperations.get("hello");
        System.out.println("useRedisDao = " + captcha);
    }

}
