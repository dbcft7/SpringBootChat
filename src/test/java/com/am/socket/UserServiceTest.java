package com.am.socket;


import com.am.socket.dao.UserMapper;
import com.am.socket.model.User;
import com.am.socket.model.UserSalt;
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

    private String username = "mazyi";
    private String password = "dbcft";
    private String email = "1721591676@qq.com";

    @Resource
    private UserService userService;

    @Resource
    private UserMapper userMapper;

    @Test
    public void userExist() throws Exception {
        boolean result = userService.userLoginForWebSocket(username, password);
        System.out.println(result);
    }

    @Test
    public void userRegister() throws Exception {
        String password = "bdZ1/vlRh9GCDEE52sw7zgpmdAEZ5pJlHUFe+/MXlplOs+Ru/hQhW1TVomDbnqezBzOU373/C07gV3AGtPg0MvPvlqJTgZp2OKJGyGmOfqmKbMHE4XyKizDfSgN9yHQ9ylRlCQxSlnnZwIvQjZhsDgJbMHSsdcNxhjUaYGLEXUU=";
        String result = userService.userRegister(username, password, email);
        System.out.println(result);
    }

    @Test
    public void userActivate() {
        String activateCode = "65236329b991541dd1f00e68edf2c446fef76738";
        String active = userService.processActivate(email, activateCode);
        System.out.println(active);
    }

    String userOne = "angle";
    String userTwo = "mazyi";

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


    @Test
    public void addSalt() {
        UserSalt userSalt = new UserSalt();
        userSalt.setSalt("123456");
        userSalt.setUsername("angle");
        userMapper.insertSaltIntoSalt("123456","angle");
        System.out.println("successful!");
    }

    @Test
    public void storeOfflineMessage() {
        String senderName = "angle";
        String receiverName = "anqi";
        String date = "2018-10-22 12:12:12";
        String message = "hello!";
        userService.storeOfflineMessage(senderName, receiverName, message, date, 0);
    }

}



