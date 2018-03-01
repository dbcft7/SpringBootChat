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

    private String userName = "angle";
    private String password = "dbcft";

    @Resource
    private UserService userService;

    @Test
    public void userExist() throws Exception {
        String result = userService.userExist(userName, password);
        System.out.println(result);
    }

}



