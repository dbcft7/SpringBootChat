package com.am.socket.service;


import com.am.socket.Application;
import com.am.socket.dao.UserMapper;
import com.am.socket.model.Comment;
import com.am.socket.model.Moment;
import com.am.socket.model.User;
import com.am.socket.model.UserSalt;
import com.am.socket.dao.CommentMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
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

    private String username = "angle";
    // password for mazyi
    //private String password = "bdZ1/vlRh9GCDEE52sw7zgpmdAEZ5pJlHUFe+/MXlplOs+Ru/hQhW1TVomDbnqezBzOU373/C07gV3AGtPg0MvPvlqJTgZp2OKJGyGmOfqmKbMHE4XyKizDfSgN9yHQ9ylRlCQxSlnnZwIvQjZhsDgJbMHSsdcNxhjUaYGLEXUU=";
    //password for angle
    private String password = "nSn/VK5vWeFcFsUcgmL7GHF/XFaY10sPjktRov4jpx2kIbXQSt/nczT9/pP7xEyYiKKPgoHu+eOVAlK1Hj4ow/1Ip1WImBbuNez8zPwUDp+cq0IViXc8wrtf6S5lUrRK0eVF57CWHAIbVc8Rq5HNzwdGYPdH2Ary9UeJVeuv93M=";
    private String email = "1721591676@qq.com";
    private String uuid = "123456";
    private String captcha = "KW2C";

    private static final String SESSION_ATTRIBUTE = "user";
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
    private MomentService momentService;

    @Resource
    private ChatService chatService;

    @Resource
    private UserMapper userMapper;

    @Resource
    private CommentService commentService;

    @Autowired
    private CommentMapper commentMapper;

    @Test
    public void userExist() throws Exception {
        boolean result = userService.userLogin(username, password, captcha, uuid, session);
        User user = userMapper.findUserFromAccount(username);
        session.setAttribute("user", user);
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
        User user = (User) session.getAttribute(SESSION_ATTRIBUTE);
        int userId = user.getId();
        String result = userService.userAddFriend(userId, userTwo);
        System.out.println(result);
    }

    @Test
    public void userFindFriend() throws Exception {
        userExist();
        User user = (User) session.getAttribute(SESSION_ATTRIBUTE);
        int userId = user.getId();
        List<User> userList = userService.userFindFriend(userId);
        for (User user1 : userList) {
            System.out.println(user.getUsername());
        }
    }


    @Test
    public void addSalt() {
        UserSalt userSalt = new UserSalt();
        userSalt.setSalt("123456");
        userSalt.setUsername("angle");
        userMapper.insertSaltIntoSalt("123456", "angle");
        System.out.println("successful!");
    }

    @Test
    public void storeOfflineMessage() {
        String senderName = "angle";
        String receiverName = "anqi";
        String date = "2018-10-22 12:12:12";
        String message = "hello!";
        chatService.storeOfflineMessage(senderName, receiverName, message, date, 0);
    }

    @Test
    public void sendMoment() throws Exception {
        userExist();
        User user = (User) session.getAttribute(SESSION_ATTRIBUTE);
        String content = "hi, I am mazyi!";
        momentService.sendMoment(user, content);
    }

    @Test
    public void getPersonalMoment() throws Exception {
        userExist();
        User user = (User) session.getAttribute(SESSION_ATTRIBUTE);
        int userId = user.getId();
        List<Moment> moments = momentService.getPersonalMoment(userId);
        for (Moment moment : moments) {
            System.out.println(moment.getUsername() + ": " + moment.getContent());
        }
    }

    @Test
    public void getFriendsMoments() throws Exception {
        userExist();
        User user = (User) session.getAttribute(SESSION_ATTRIBUTE);
        List<Moment> moments = momentService.getFriendsMoment(user);
        for (Moment moment : moments) {
            System.out.println(moment.getUsername() + ": " + moment.getContent());
        }
    }

    @Test
    public void deleteMoments() throws Exception {
        userExist();
        Moment moment = new Moment();
        moment.setMomentId(2);
        moment.setUserId(27);
        User user = (User) session.getAttribute(SESSION_ATTRIBUTE);
        int userId = user.getId();
        String result = momentService.deleteMoment(userId, moment);
        System.out.println(result);
    }

    @Test
    public void sendCommentToMoment() throws Exception {
        userExist();
        String momentId = "1";
        String commentContent = "Say hi!";
        User user = (User) session.getAttribute(SESSION_ATTRIBUTE);
        commentService.sendComment(momentId, user.getId(), user.getUsername(), commentContent);
        System.out.println();
    }


    @Test
    public void sendCommentToComment() throws Exception {
        userExist();
        String momentId = "1";
        String TargetCommentId = "5b1eeba6b30284114c483d97";
        String commentContent = "Comment to Comment!";
        User user = (User) session.getAttribute(SESSION_ATTRIBUTE);
        commentService.sendComment(momentId, TargetCommentId, user.getId(), user.getUsername(), commentContent);
    }

    @Test
    public void deleteComment() throws Exception {
        String commentId = "5b1eeba6b30284114c483d97";
        commentService.deleteComment(commentId);
    }

    @Test
    public void getComment() {
        String momentId = "1";
        List<Comment> result = commentService.getComments(momentId);
        for (Comment comment : result) {
            System.out.println(comment.getUsername() + " comment to " + comment.getTargetUsername() + ": " + comment.getComment());
        }
    }

    @Test
    public void getFriend() {
        int userId = 28;
        List<Integer> friendList = userMapper.findUserIdFromFriend(userId);
        for (int id : friendList) {
            System.out.println(id);
        }
    }
}



