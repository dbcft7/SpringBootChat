package com.am.socket.dao;
import com.am.socket.model.*;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;


public interface UserMapper {
    void insertUserIntoAccount(User user);
    User findUserFromAccount(@Param("username") String username);
    User findEmailFromAccount(@Param("email") String email);
    User fineUserIdFromAccount(@Param("userId") int userId);
    List<User> findMoreUserFromAccount(List<String> moreUsername);

    void insertUserIntoFriend(@Param("userId") int userId, @Param("friendId") int friendId);
    List<User> findUserFromFriend(@Param("userId") int userId);
    List<Integer> findUserIdFromFriend(@Param("userId") int userId);

    void insertSaltIntoSalt(@Param("username") String username, @Param("salt") String salt);
    UserSalt findSaltFromSalt(@Param("username") String username);

    void activeAccount(@Param("userId") int userId);

    void insertCaptchaIntoCaptcha(@Param("uuid") String uuid, @Param("captcha") String captcha);
    Captcha findCaptchaFromCaptcha(@Param("uuid") String uuid);

}
