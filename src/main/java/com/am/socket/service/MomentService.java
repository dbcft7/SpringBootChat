package com.am.socket.service;

import com.am.socket.dao.MomentMapper;
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
    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Resource
    private UserMapper userMapper;
    @Resource
    private MomentMapper momentMapper;

    public String sendMoment(User user, String content) {
        Date pubtime = new Date();
        Moment moment = new Moment();
        moment.setContent(content);
        moment.setPubtime(pubtime);
        moment.setUserId(user.getId());
        moment.setUsername(user.getUsername());
        momentMapper.insertMomentIntoMoment(moment);
        log.info("sent moment successfully!");
        return "sent moment successfully!";
    }

    //get own moments
    public List<Moment> getPersonalMoment(int userId) {
        List<Moment> moments = momentMapper.findMomentsFromMoment(userId);
        log.info("get personal moments successfully!");
        return moments;
    }

    //get friends' and own moments together
    public List<Moment> getFriendsMoment (User user) {
        List<Integer> userIdList = userMapper.findUserIdFromFriend(user.getId());
        userIdList.add(user.getId());
        List<Moment> moments = momentMapper.findFriendMomentsFromMoment(userIdList);
        log.info("get friend's moments successfully!");
        return moments;
    }

    public String deleteMoment (int userId, Moment moment) {
        if (userId == moment.getUserId()) {
            momentMapper.deleteMomentFromMoment(moment);
            log.info("deleted moment successfully!");
            return "deleted moment successfully!";
        } else {
            log.info("you cannot delet other's moment!");
            return "you cannot delet other's moment!";
        }
    }
}
