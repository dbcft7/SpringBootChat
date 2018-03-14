package com.am.socket.service;
import com.am.socket.model.UserSalt;
import com.am.socket.util.Hash;
import com.am.socket.dao.UserMapper;
import com.am.socket.model.User;
import com.am.socket.util.RSA;
import com.am.socket.util.SendEmail;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

import static com.am.socket.util.RSA.decrypt;


@Service
public class UserService {
    @Resource
    private UserMapper userMapper;

    public boolean findUserIsTrue(String username, String password) throws Exception {
        User userFromAccount = userMapper.findUserFromAccount(username);
        UserSalt userSalt = userMapper.findSaltFromSalt(username);
        String passwordHashed = "";
        if (userSalt != null) {
            String salt = userSalt.getSalt();
            passwordHashed = Hash.encrypt(password + salt);
        }

        if (userFromAccount == null) {
            return false;
        } else if (username.equals(userFromAccount.getUsername()) && passwordHashed.equals(userFromAccount.getPassword())) {
            return true;
        } else {
            return false;
        }
    }

    public List<String> moreUserExist(List<String> username) {
        List<User> userList = userMapper.findMoreUserFromAccount(username);
        List<String> user = new ArrayList<>();
        for (int i=0; i<userList.size(); i++) {
            user.add(userList.get(i).getUsername());
        }
        return user;
    }

    public String userRegister(String username, String passwordRSA, String email) throws Exception {
        User user = new User();
        user.setUsername(username);
        String password = new String(RSA.decrypt(passwordRSA, RSA.getPrivateKey(RSA.privateKeyString)));
        System.out.println("**********************" + password);
        String salt = Hash.generateSalt();
        String passwordSalted = Hash.encrypt(password + salt);
        user.setPassword(passwordSalted);
        user.setEmail(email);
        String activeCode = Hash.encrypt(email);
        System.out.println("**********************" + activeCode);
        user.setActiveCode(activeCode);
        User userFromAccount = userMapper.findUserFromAccount(username);

        if (userFromAccount == null) {
            processRegister(email,activeCode);
            userMapper.insertUserIntoAccount(user);
            userMapper.insertSaltIntoSalt(username, salt);
            return "Register successfully!";
        } else {
            return "the username is already existed, please change to another one!";
        }
    }

    public String userAddFriend(String username, String friendName) {
        User userFromAccount = userMapper.findUserFromAccount(username);
        User friendFromAccount = userMapper.findUserFromAccount(friendName);

        if (friendFromAccount == null) {
            return "the user you want to add to a friend is not exist!";
        }

        List<User> friendList = userFindFriend(username);

        for (User friend : friendList) {
            if (friend.getUsername().equals(friendName)) {
                return "you added this friend before, please do not add it again!";
            }
        }

        int userId = userFromAccount.getId();
        int friendId = friendFromAccount.getId();
        userMapper.insertUserIntoFriend(userId, friendId);
        userMapper.insertUserIntoFriend(friendId, userId);
        return "add friend successfully!";
    }

    public List<User> userFindFriend(String username) {
        User userFromAccount = userMapper.findUserFromAccount(username);
        int userId = userFromAccount.getId();
        List<User> friendList = userMapper.findUserFromFriend(userId);
        return friendList;
    }

    public void processRegister(String email, String activeCode) throws Exception {
        StringBuffer content = new StringBuffer();
        content.append("点击下面链接激活账号，否则重新注册账号，链接只能使用一次，请尽快激活！</br>");
        content.append("<a href=\"http://127.0.0.1:8080/user/activate?email=");
        content.append(email);
        content.append("&activeCode=");
        content.append(activeCode);
        content.append("\">http://localhost:8080/user/activate?email=");
        content.append(email);
        content.append("&activeCode=");
        content.append(activeCode);
        content.append("</a>");

        SendEmail.send(email, content.toString());
        System.out.println("发送邮件");
    }

    public String processActivate(String email, String activeCode) {
        User user = userMapper.findEmailFromAccount(email);
        if (user != null) {
            if (user.getActive() == null) {
                if (user.getActiveCode().equals(activeCode)) {
                    userMapper.activeAccount(user.getId());
                    return "Activate successfully!";
                } else {
                    return "Activation code is wrong!";
                }
            } else {
                return "You have already activated before!";
            }
        } else {
            return "the user is not exist!";
        }
    }
}
