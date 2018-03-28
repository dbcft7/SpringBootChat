package com.am.socket.controller;

import com.am.socket.model.User;
import com.am.socket.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserCtrl {

    @Resource
    private UserService userService;

    @GetMapping("/login")
    public boolean login(@RequestParam("username") String username, @RequestParam("password") String password, @RequestParam("captcha") String captcha, @RequestParam("uuid") String uuid, HttpSession session) throws Exception {
        return userService.userLogin(username, password, captcha, uuid, session);
    }

    @GetMapping("/logout")
    public String userLogout(HttpSession session) {
        return userService.userLogout(session);
    }

    //   parameter from front-end: ["mazy","angle"]
    @PostMapping("/checkUser")
    public List<String> moreUserFind(@RequestBody List<String> users) {
        return userService.moreUserExist(users);
    }

    @PostMapping("/register")
    public String userRegister(@RequestParam("username") String username, @RequestParam("password") String password, @RequestParam("email") String email) throws Exception {
        return userService.userRegister(username, password, email);
    }

    @GetMapping("/addFriend")
    public String userAddFriend(HttpSession session, @RequestParam("friendName") String friendName) {
        User user = (User) session.getAttribute(User.SESSION_ATTRIBUTE);
        int userId = user.getId();
        return userService.userAddFriend(userId, friendName);
    }

    @GetMapping("/findFriend")
    public List<User> userFindFriend(HttpSession session) {
        User user = (User) session.getAttribute(User.SESSION_ATTRIBUTE);
        int userId = user.getId();
        return userService.userFindFriend(userId);
    }

    @GetMapping("/activate")
    public String userActivate(@RequestParam("email") String email, @RequestParam("activeCode") String activeCode) {
        return userService.processActivate(email, activeCode);
    }

    @GetMapping("/captcha")
    public void generateCaptcha(@RequestParam("uuid") String uuid, HttpServletRequest request, HttpServletResponse response) throws IOException {
        userService.generateCaptcha(uuid, request, response);
    }
}
