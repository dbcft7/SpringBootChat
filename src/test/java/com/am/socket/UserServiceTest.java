package com.am.socket;


import com.am.socket.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class UserServiceTest {

    private String username = "tuantuan";
    private String password = "111111";

    @Resource
    private UserService userService;

    @Test
    public void userExist() throws Exception {
        String result = userService.userExist(username, password);
        System.out.println(result);
    }

    @Test
    public void userRegister() {
        String result = userService.userRegister(username, password);
        System.out.println(result);
    }

}



