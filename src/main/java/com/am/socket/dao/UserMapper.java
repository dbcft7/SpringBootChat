package com.am.socket.dao;
import com.am.socket.model.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface UserMapper {
    User findUser(@Param("username") String userName);
    List<User> findMoreUser(List<String> moreUserName);
    User findUserFromAccount(@Param("username") String username);
}
