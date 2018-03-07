package com.am.socket;


import com.am.socket.model.User;
import com.am.socket.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class UserServiceTest {

    private String username = "angle";
    private String password = "dbcft";

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

    String userOne = "angle";
    String userTwo = "tuan";

    @Test
    public void userAddFriend() {
        String result = userService.userAddFriend(userOne, userTwo);
        System.out.println(result);
    }

    @Test
    public void userFindFriend() {
        List<User> userList = userService.userFindFriend(username);
        for (User user : userList) {
            System.out.println(user.getUsername());
        }
    }

}



