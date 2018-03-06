package com.am.socket.service;
import com.am.socket.util.Hash;
import com.am.socket.dao.UserMapper;
import com.am.socket.model.User;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
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
        User userFromDB = userMapper.findUserFromAccount(username);

        if (userFromDB == null) {
            return false;
        } else if (username.equals(userFromDB.getUsername()) && password.equals(userFromDB.getPassword())) {
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
        User userFromDB = userMapper.findUserFromAccount(username);

        if (userFromDB == null) {
            userMapper.insertUser2Account(user);
            return "Register successfully!";
        } else {
            return "the username is already existed, please change to another one!";
        }
    }

}
