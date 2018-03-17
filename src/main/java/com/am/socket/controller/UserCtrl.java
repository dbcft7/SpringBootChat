package com.am.socket.controller;
import com.am.socket.model.User;
import com.am.socket.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserCtrl {

    @Resource
    private UserService user;

    @GetMapping("/login")
    public boolean userFind(@RequestParam("username") String username, @RequestParam("password") String password, @RequestParam("captcha") String captcha, @RequestParam("uuid") String uuid) throws Exception {
        return user.findUserIsTrue(username, password, captcha, uuid);
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

    @PostMapping("/addFriend")
    public String userAddFriend(@RequestParam("username") String username, @RequestParam("friendName") String friendName) {
        return user.userAddFriend(username, friendName);
    }

    @PostMapping("/findFriend")
    public List<User> userFindFriend(@RequestParam("username") String username) {
        return user.userFindFriend(username);
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
