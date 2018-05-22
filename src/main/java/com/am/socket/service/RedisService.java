package com.am.socket.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
public class RedisService {
    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    public String generateCaptcha(String uuid, HttpServletRequest request, HttpServletResponse response) throws IOException {
        BufferedImage bufferedImage = new BufferedImage(68, 22, BufferedImage.TYPE_INT_BGR);
        Graphics graphics = bufferedImage.getGraphics();
        Color color = new Color(79, 230, 255);
        graphics.setColor(color);
        graphics.fillRect(0, 0, 68, 22);
        char[] chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray();
        Random random = new Random();
        int index;
        StringBuffer stringBuffer = new StringBuffer();

        //generate random string and image
        for (int i = 0; i < 4; i++) {
            index = random.nextInt(chars.length);
            graphics.setColor(new Color(random.nextInt(88), random.nextInt(188), random.nextInt(255)));
            graphics.drawString(chars[index]+"", (i * 15) +3, 18);
            stringBuffer.append(chars[index]);
        }
        String captcha = stringBuffer.toString();

        // store captcha into redis
        ValueOperations<String,Object> valueOperations = redisTemplate.opsForValue();
        valueOperations.set(uuid, captcha, 180, TimeUnit.SECONDS);

        ImageIO.write(bufferedImage, "JPG", response.getOutputStream());
        return uuid;
    }

    public String getCaptcha(String uuid) {
        ValueOperations<String,Object> valueOperations = redisTemplate.opsForValue();
        return (String) valueOperations.get(uuid);
    }
}
