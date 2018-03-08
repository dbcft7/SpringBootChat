package com.am.socket.service;
import com.am.socket.util.Hash;
import com.am.socket.dao.UserMapper;
import com.am.socket.model.User;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;


@Service
public class UserService {
    @Resource
    private UserMapper userMapper;

    public String userExist(String username, String password) throws Exception {
        User userFromDB = userMapper.findUser(username);
        String passwordHashed = Hash.encrypt(password);

        if (userFromDB == null) {
            return "the account is not exist!";
        } else if(username.equals(userFromDB.getUsername()) && passwordHashed.equals(userFromDB.getPassword())){
            return "login successfully!";
        } else {
            return "password is wrong!";
        }
    }

    public boolean findUserIsTrue(String username, String password) throws Exception {
        User userFromAccount = userMapper.findUserFromAccount(username);

        if (userFromAccount == null) {
            return false;
        } else if (username.equals(userFromAccount.getUsername()) && password.equals(userFromAccount.getPassword())) {
            return true;
        } else {
            return false;
        }
    }

    public List<String> moreUserExist(List<String> username) {
        List<User> userList = userMapper.findMoreUser(username);
        List<String> user = new ArrayList<>();
        for (int i=0; i<userList.size(); i++) {
            user.add(userList.get(i).getUsername());
        }
        return user;
    }

    public String userRegister(String username, String password) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        User userFromAccount = userMapper.findUserFromAccount(username);

        if (userFromAccount == null) {
            userMapper.insertUserIntoAccount(user);
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

}
