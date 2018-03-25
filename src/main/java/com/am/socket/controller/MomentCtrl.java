package com.am.socket.controller;

import com.am.socket.model.Moment;
import com.am.socket.model.User;
import com.am.socket.service.MomentService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequestMapping("/moment")
public class MomentCtrl {
    @Resource
    private MomentService momentService;

    private static final String SESSION_ATTRIBUTE = "user";

    @GetMapping("/sendMoment")
    public String sendMoment(@RequestParam("content") String content, HttpSession session) {
        User user = (User) session.getAttribute(SESSION_ATTRIBUTE);
        return momentService.sendMoment(user, content);
    }

    @GetMapping("/getPersonalMoments")
    public List<Moment> getPersonalMoments(HttpSession session) {
        User user = (User) session.getAttribute(SESSION_ATTRIBUTE);
        int userId = user.getId();
        return momentService.getPersonalMoment(userId);
    }

    @GetMapping("/getFriendsMoments")
    public List<Moment> getFriendsMoments(HttpSession session) {
        User user = (User) session.getAttribute(SESSION_ATTRIBUTE);
        return momentService.getFriendsMoment(user);
    }

    @GetMapping("/deleteMoment")
    public String deleteMoment(HttpSession session, Moment moment) {
        User user = (User) session.getAttribute(SESSION_ATTRIBUTE);
        int userId = user.getId();
        return momentService.deleteMoment(userId, moment);
    }
}
