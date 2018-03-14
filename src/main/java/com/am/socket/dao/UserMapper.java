package com.am.socket.dao;
import com.am.socket.model.User;
import com.am.socket.model.UserSalt;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;


public interface UserMapper {
    void insertUserIntoAccount(User user);
    User findUserFromAccount(@Param("username") String username);
    User findEmailFromAccount(@Param("email") String email);
    List<User> findMoreUserFromAccount(List<String> moreUsername);

    void insertUserIntoFriend(@Param("userId") int userId, @Param("friendId") int friendId);
    List<User> findUserFromFriend(@Param("userId") int userId);

    void insertSaltIntoSalt(@Param("username") String username, @Param("salt") String salt);
    UserSalt findSaltFromSalt(@Param("username") String username);

    void activeAccount(@Param("userId") int userId);

}
