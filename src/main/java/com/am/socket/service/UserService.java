package com.am.socket.service;
import com.am.socket.Util.Hash;
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
        User userinsql = userMapper.findUser(username);
        String passwordHashed = Hash.encrypt(password);

        if(userinsql == null) return "the account is not exist!";
        else if(username.equals(userinsql.getUsername()) && passwordHashed.equals(userinsql.getPassword())){
            return "login successfully!";
        }else{
            return "password is wrong!";
        }
    }

    public List<String> moreUserExist(List<String> username){
        List<User> userList = userMapper.findMoreUser(username);
        List<String> user = new ArrayList<>();
        for(int i=0; i<userList.size(); i++){
            user.add(userList.get(i).getUsername());
        }
        return user;
    }

}
