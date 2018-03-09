package com.am.socket.dao;
import com.am.socket.model.User;
import com.am.socket.model.UserSalt;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;


public interface UserMapper {
    User findUser(@Param("username") String username);
    List<User> findMoreUser(List<String> moreUsername);

    User findUserFromAccount(@Param("username") String username);
    void insertUserIntoAccount(User user);

    void insertUserIntoFriend(@Param("userId") int userId, @Param("friendId") int friendId);
    List<User> findUserFromFriend(@Param("userId") int userId);

    void insertSaltIntoSalt(@Param("username") String username, @Param("salt") String salt);
    UserSalt findSaltFromSalt(@Param("username") String username);

}
