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

    private String username = "angle";
    private String password = "dbcft";

    @Resource
    private UserService userService;

    @Resource
    private UserMapper userMapper;

    @Test
    public void userExist() throws Exception {
        boolean result = userService.findUserIsTrue(username, password);
        System.out.println(result);
    }

    @Test
    public void userRegister() throws Exception {
        String password = "pd98AP9Zoju0K+zhuW9zVVYg3MHzWfCuXz3eg4CJZwYggrc10u+g9tGSFAgZ6zXLdLWmRd0ewu3fcthb5SrYNRtBMvwlnFD7c1ygfK/8TdsG8pX5BfEbeSJsCNolj4pLcVr4T0s+20PLicyZJzudOvRecjxddIPmNaZ/ZgU6chg=";
        String result = userService.userRegister("anqi", password);
        System.out.println(result);
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

}



