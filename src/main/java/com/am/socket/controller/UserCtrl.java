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
    private UserService user;

    @GetMapping("/login")
    public boolean login(@RequestParam("username") String username, @RequestParam("password") String password, @RequestParam("captcha") String captcha, @RequestParam("uuid") String uuid, HttpSession session) throws Exception {
        return user.findUserIsTrue(username, password, captcha, uuid, session);
    }

    @GetMapping("/logout")
    public String userLogout(HttpSession session) {
        return user.userLogout(session);
    }

    //   parameter from front-end: ["mazy","angle"]
    @PostMapping("/checkUser")
    public List<String> moreUserFind(@RequestBody List<String> users) {
        return user.moreUserExist(users);
    }

    @PostMapping("/register")
    public String userRegister(@RequestParam("username") String username, @RequestParam("password") String password, @RequestParam("email") String email) throws Exception {
        return user.userRegister(username, password, email);
    }

    @GetMapping("/addFriend")
    public String userAddFriend(HttpSession session, @RequestParam("friendName") String friendName) {
        return user.userAddFriend(session, friendName);
    }

    @GetMapping("/findFriend")
    public List<User> userFindFriend(HttpSession session) {
        return user.userFindFriend(session);
    }

    @GetMapping("/activate")
    public String userActivate(@RequestParam("email") String email, @RequestParam("activeCode") String activeCode) {
        return user.processActivate(email, activeCode);
    }

    @GetMapping("/captcha")
    public void generateCaptcha(@RequestParam("uuid") String uuid, HttpServletRequest request, HttpServletResponse response) throws IOException {
        user.generateCaptcha(uuid, request, response);
    }

}
