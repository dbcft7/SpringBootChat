package com.am.socket.service;
import com.am.socket.model.*;
import com.am.socket.util.Hash;
import com.am.socket.dao.UserMapper;
import com.am.socket.util.RSA;
import com.am.socket.util.SendEmail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

import static com.am.socket.util.RSA.decrypt;


@Service
public class UserService {
    private Logger log = LoggerFactory.getLogger(this.getClass());

    private final static String URL = "http://127.0.0.1:8080/user/activate";

    @Resource
    private UserMapper userMapper;

    @Resource
    private RedisService redisService;

    public boolean userLogin(String username, String passwordRSA, String captchaString, String uuid, HttpSession session) throws Exception {
        User userFromDB = userMapper.findUserFromAccount(username);
        UserSalt userSalt = userMapper.findSaltFromSalt(username);
        String password = new String(decrypt(passwordRSA, RSA.getPrivateKey(RSA.privateKeyString)));
        String captcha = redisService.getCaptcha(uuid);
        if (captcha == null || !captchaString.equalsIgnoreCase(captcha)) {
            log.info("the captcha is wrong!");
            return false;
        }

        String passwordHashed = "";
        if (userSalt != null) {
            String salt = userSalt.getSalt();
            passwordHashed = Hash.encrypt(password + salt);
        }
        if (userFromDB == null) {
            log.info("the user is not exist!");
            return false;
        } else if (username.equals(userFromDB.getUsername()) && passwordHashed.equals(userFromDB.getPassword())) {
            log.info("http login successfully!");
            session.setAttribute("user", userFromDB);
            return true;
        } else {
            log.info("username or password is wrong!");
            return false;
        }
    }

    public boolean userLoginForWebSocket(String username, String password) throws Exception {
        User userFromAccount = userMapper.findUserFromAccount(username);
        UserSalt userSalt = userMapper.findSaltFromSalt(username);
        String passwordHashed = "";
        if (userSalt != null) {
            String salt = userSalt.getSalt();
            passwordHashed = Hash.encrypt(password + salt);
        }
        if (userFromAccount == null) {
            log.info("the user is not exist!");
            return false;
        } else if (username.equals(userFromAccount.getUsername()) && passwordHashed.equals(userFromAccount.getPassword())) {
            log.info("webSocket login successfully!");
            return true;
        } else {
            log.info("userLoginForWebSocket: username or password is wrong!");
            return false;
        }
    }

    public String userLogout(HttpSession session) {
        session.invalidate();
        return "logout successfully!";
    }

    public boolean findUserFromDB(String username) {
        return (userMapper.findUserFromAccount(username) != null);
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
        log.info("userRegister   password is: " + password);
        String salt = Hash.generateSalt();
        String passwordSalted = Hash.encrypt(password + salt);
        user.setPassword(passwordSalted);
        user.setEmail(email);
        String activeCode = Hash.encrypt(email);
        log.info("userRegister   active code is: " + activeCode);
        user.setActiveCode(activeCode);
        User userFromAccount = userMapper.findUserFromAccount(username);

        if (userFromAccount == null) {
            sendEmailtoUser(email,activeCode);
            userMapper.insertUserIntoAccount(user);
            userMapper.insertSaltIntoSalt(username, salt);
            return "Register successfully!";
        } else {
            return "the username is already existed, please change to another one!";
        }
    }

    public String userAddFriend(int userId, String friendName) {
        User friendFromAccount = userMapper.findUserFromAccount(friendName);

        if (friendFromAccount == null) {
            return "the user you want to add to a friend is not exist!";
        }

        List<User> friendList = userFindFriend(userId);

        for (User friend : friendList) {
            if (friend.getUsername().equals(friendName)) {
                return "you added this friend before, please do not add it again!";
            }
        }

        int friendId = friendFromAccount.getId();
        userMapper.insertUserIntoFriend(userId, friendId);
        userMapper.insertUserIntoFriend(friendId, userId);
        return "add friend successfully!";
    }

    public List<User> userFindFriend(int userId) {
        List<User> friendList = userMapper.findUserFromFriend(userId);
        return friendList;
    }

    private void sendEmailtoUser(String email, String activeCode) throws Exception {
        StringBuffer content = new StringBuffer();
        content.append("点击下面链接激活账号，否则重新注册账号，链接只能使用一次，请尽快激活！</br>");
        content.append("<a href=\"");
        content.append(URL);
        content.append("?email=");
        content.append(email);
        content.append("&activeCode=");
        content.append(activeCode);
        content.append("\">请点击此链接激活账号</a>");

        SendEmail.send(email, content.toString());
        log.info("To {} email sent successfully!", email);
    }

    public String processActivate(String email, String activeCode) {
        User user = userMapper.findEmailFromAccount(email);
        if (user != null) {
            if (user.getActive() != User.ACTIVATE) {
                if (user.getActiveCode().equals(activeCode)) {
                    userMapper.activeAccount(user.getId());
                    return "Activated successfully!";
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
