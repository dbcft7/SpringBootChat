package com.am.socket.controller;
import com.am.socket.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserCtrl {

    @Resource
    private UserService user;

    @PostMapping("/login")
    public String UserFind(@RequestParam("username") String username, @RequestParam("password") String password) throws Exception {
        return user.userExist(username,password);
    }
    //   parameter from front-end: ["mazy","angle"]
    @PostMapping("/checkuser")
    public List<String> MoreUserFind(@RequestBody List<String> users){
        return user.moreUserExist(users);
    }

}
