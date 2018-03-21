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
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;
import java.util.Enumeration;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class UserServiceTest {

    private String username = "mazyi";
    private String password = "bdZ1/vlRh9GCDEE52sw7zgpmdAEZ5pJlHUFe+/MXlplOs+Ru/hQhW1TVomDbnqezBzOU373/C07gV3AGtPg0MvPvlqJTgZp2OKJGyGmOfqmKbMHE4XyKizDfSgN9yHQ9ylRlCQxSlnnZwIvQjZhsDgJbMHSsdcNxhjUaYGLEXUU=";
    private String email = "1721591676@qq.com";
    private HttpSession session = new HttpSession() {
        private String key;
        private Object value;
        @Override
        public long getCreationTime() {
            return 0;
        }

        @Override
        public String getId() {
            return null;
        }

        @Override
        public long getLastAccessedTime() {
            return 0;
        }

        @Override
        public ServletContext getServletContext() {
            return null;
        }

        @Override
        public void setMaxInactiveInterval(int i) {

        }

        @Override
        public int getMaxInactiveInterval() {
            return 0;
        }

        @Override
        public HttpSessionContext getSessionContext() {
            return null;
        }

        @Override
        public Object getAttribute(String s) {
            if (s.equals(key)) {
                return value;
            } else {
                return null;
            }
        }

        @Override
        public Object getValue(String s) {
            return null;
        }

        @Override
        public Enumeration<String> getAttributeNames() {
            return null;
        }

        @Override
        public String[] getValueNames() {
            return new String[0];
        }

        @Override
        public void setAttribute(String s, Object o) {
            this.key = s;
            this.value = o;
        }

        @Override
        public void putValue(String s, Object o) {

        }

        @Override
        public void removeAttribute(String s) {

        }

        @Override
        public void removeValue(String s) {

        }

        @Override
        public void invalidate() {
            key = null;
            value = null;
        }

        @Override
        public boolean isNew() {
            return false;
        }
    };

    @Resource
    private UserService userService;

    @Resource
    private UserMapper userMapper;

    @Test
    public void userExist() throws Exception {
        boolean result = userService.findUserIsTrue(username, password,"TW6T", "02a123d2-acf0-46b2-9ba7-43eaa0b34f30", session);
        User user = userMapper.findUserFromAccount(username);
        session.setAttribute("user",user);
        System.out.println(result);
    }


    @Test
    public void userRegister() throws Exception {
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
        String result = userService.userAddFriend(session, userTwo);
        System.out.println(result);
    }

    @Test
    public void userFindFriend() throws Exception {
        userExist();
        List<User> userList = userService.userFindFriend(session);
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



