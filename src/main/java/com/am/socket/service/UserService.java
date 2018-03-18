package com.am.socket.service;
import com.am.socket.model.OfflineMessage;
import com.am.socket.model.UserSalt;
import com.am.socket.util.Hash;
import com.am.socket.dao.UserMapper;
import com.am.socket.model.User;
import com.am.socket.util.RSA;
import com.am.socket.util.SendEmail;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
    @Resource
    private UserMapper userMapper;
    private static final int ACTIVATE = 1;
    private static final int NOTRECEIVED = 0;
    private static final int RECEIVED = 1;

    public final static String URL = "http://127.0.0.1:8080/user/activate";

    public boolean findUserIsTrue(String username, String passwordRSA, String captcha, String uuid) throws Exception {
        User userFromAccount = userMapper.findUserFromAccount(username);
        UserSalt userSalt = userMapper.findSaltFromSalt(username);
        String password = new String(decrypt(passwordRSA, RSA.getPrivateKey(RSA.privateKeyString)));
        String captchaFromDB = userMapper.findCaptchaFromCaptcha(uuid).getCaptcha();
        System.out.println("**************************8captcha from DB is:" + captchaFromDB);
        String passwordHashed = "";
        if (userSalt != null) {
            String salt = userSalt.getSalt();
            passwordHashed = Hash.encrypt(password + salt);
        }
        if (!captcha.equalsIgnoreCase(captchaFromDB)) {
            System.out.println("the captcha is wrong!");
            return false;
        }
        if (userFromAccount == null) {
            System.out.println("the user is not exist!");
            return false;
        } else if (username.equals(userFromAccount.getUsername()) && passwordHashed.equals(userFromAccount.getPassword())) {
            System.out.println("login successfully!");
            return true;
        } else {
            System.out.println("findUserIsTrue: username or password is wrong!");
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
            System.out.println("the user is not exist!");
            return false;
        } else if (username.equals(userFromAccount.getUsername()) && passwordHashed.equals(userFromAccount.getPassword())) {
            System.out.println("login successfully!");
            return true;
        } else {
            System.out.println("userLoginForWebSocket: username or password is wrong!");
            return false;
        }
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
            sendEmailtoUser(email,activeCode);
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
        System.out.println("email sent successfully!");
    }

    public String processActivate(String email, String activeCode) {
        User user = userMapper.findEmailFromAccount(email);
        if (user != null) {
            if (user.getActive() != ACTIVATE) {
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
        userMapper.insertCaptchaIntoCaptcha(uuid, captcha);

        ImageIO.write(bufferedImage, "JPG", response.getOutputStream());
        return uuid;
    }

    public String storeOfflineMessage(String senderName, String receiverName, String message, String dateTime) {
        String returnMessage = "";
        if (userMapper.findUserFromAccount(receiverName) == null) {
            returnMessage = "the user you want to send message to is not exist!";
            return returnMessage;
        }

        OfflineMessage offlineMessage = new OfflineMessage();
        int senderId = userMapper.findUserFromAccount(senderName).getId();
        int receiverId = userMapper.findUserFromAccount(receiverName).getId();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date sendTime = new Date();

        try {
            sendTime = dateFormat.parse(dateTime);
            System.out.println("***************** send time is: " + sendTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        offlineMessage.setSendTime(sendTime);
        offlineMessage.setSenderId(senderId);
        offlineMessage.setReceiverId(receiverId);
        offlineMessage.setReceiveState(NOTRECEIVED);
        offlineMessage.setOfflineMessage(message);
        userMapper.insertMessageIntoOfflineMessage(offlineMessage);
        System.out.println("*************** store offline message successfully!");
        returnMessage = "sent offline message successfully!";
        return returnMessage;
    }

    public List<String> sendOfflineMessage(String receiverName) {
        int receiverId = userMapper.findUserFromAccount(receiverName).getId();
        List<OfflineMessage> offlineMessages = userMapper.findMessageFromOfflineMessage(receiverId, NOTRECEIVED);
        List<String> sendMessage = new ArrayList<>();
        for (OfflineMessage message : offlineMessages) {
            String senderName = userMapper.fineUserIdFromAccount(message.getSenderId()).getUsername();
            String messageFormat = senderName + ": " + message.getOfflineMessage();
            sendMessage.add(messageFormat);
            message.setReceiveState(RECEIVED);
            Date receivedTime = new Date();
            message.setReceiveTime(receivedTime);
            userMapper.updateSendStateOfOfflineMessage(message);
        }
        return sendMessage;
    }

}
