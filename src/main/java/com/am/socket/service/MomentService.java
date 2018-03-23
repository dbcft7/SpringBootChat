package com.am.socket.service;

import com.am.socket.dao.UserMapper;
import com.am.socket.model.Moment;
import com.am.socket.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class MomentService {
    @Resource
    private UserMapper userMapper;

    private static final String SESSION_ATTRIBUTE = "user";
    private Logger log = LoggerFactory.getLogger(this.getClass());

    public String sendMoment(HttpSession session, String content) {
        Date pubtime = new Date();
        User user = (User) session.getAttribute(SESSION_ATTRIBUTE);
        Moment moment = new Moment();
        moment.setContent(content);
        moment.setPubtime(pubtime);
        moment.setUserId(user.getId());
        moment.setUsername(user.getUsername());
        userMapper.insertMomentIntoMoment(moment);
        log.info("sent moment successfully!");
        return "sent moment successfully!";
    }

    //get own moments
    public List<Moment> getPersonalMoment(HttpSession session) {
        User user = (User) session.getAttribute(SESSION_ATTRIBUTE);
        List<Moment> moments = userMapper.findMomentsFromMoment(user.getId());
        log.info("get personal moments successfully!");
        return moments;
    }

    //get friends' and own moments together
    public List<Moment> getFriendsMoment (HttpSession session) {
        User user = (User) session.getAttribute(SESSION_ATTRIBUTE);
        List<User> users= userMapper.findUserFromFriend(user.getId());
        users.add(user);
        List<Integer> userIdList = new ArrayList<>();
        for (User user1 : users) {
            userIdList.add(user1.getId());
        }
        List<Moment> moments = userMapper.findFriendMomentsFromMoment(userIdList);
        log.info("get friend's moments successfully!");
        return moments;
    }

    public String deleteMoment (HttpSession session, Moment moment) {
        User user = (User) session.getAttribute(SESSION_ATTRIBUTE);
        if (user.getId() == moment.getUserId()) {
            userMapper.deleteMomentFromMoment(moment);
            log.info("deleted moment successfully!");
            return "deleted moment successfully!";
        } else {
            log.info("you cannot delet other's moment!");
            return "you cannot delet other's moment!";
        }
    }
}
