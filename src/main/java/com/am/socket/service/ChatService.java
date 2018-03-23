package com.am.socket.service;

import com.am.socket.dao.UserMapper;
import com.am.socket.model.OfflineMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ChatService {
    @Resource
    private UserMapper userMapper;

    private static final int NOTRECEIVED = 0;
    private static final int RECEIVED = 1;

    private Logger log = LoggerFactory.getLogger(this.getClass());

    public String storeOfflineMessage(String senderName, String receiverName, String message, String dateTime, int receiveState) {
        if (userMapper.findUserFromAccount(receiverName) == null) {
            return "the user you want to send message to is not exist!";
        }

        OfflineMessage offlineMessage = new OfflineMessage();
        int senderId = userMapper.findUserFromAccount(senderName).getId();
        int receiverId = userMapper.findUserFromAccount(receiverName).getId();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date sendTime = new Date();

        try {
            sendTime = dateFormat.parse(dateTime);
            log.info("send time is: " + sendTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        offlineMessage.setSendTime(sendTime);
        offlineMessage.setSenderId(senderId);
        offlineMessage.setReceiverId(receiverId);
        offlineMessage.setReceiveState(receiveState);
        offlineMessage.setOfflineMessage(message);
        userMapper.insertMessageIntoOfflineMessage(offlineMessage);
        log.info("*************** store offline message successfully!");
        return "sent offline message successfully!";
    }

    public List<String> getOfflineMessage(String receiverName) {
        int receiverId = userMapper.findUserFromAccount(receiverName).getId();
        List<OfflineMessage> offlineMessages = userMapper.findMessageFromOfflineMessage(receiverId, NOTRECEIVED);
        List<String> sendMessage = new ArrayList<>();
        for (OfflineMessage message : offlineMessages) {
            String senderName = userMapper.fineUserIdFromAccount(message.getSenderId()).getUsername();
            String messageFormat = senderName + ": " + message.getOfflineMessage();
            sendMessage.add(messageFormat);
            message.setReceiveState(RECEIVED);
            Date receivedTime = new Date();
            message.setReceiveTime(receivedTime);
            userMapper.updateSendStateOfOfflineMessage(message);
        }
        return sendMessage;
    }
}
