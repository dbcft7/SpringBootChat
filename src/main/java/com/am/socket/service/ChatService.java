package com.am.socket.service;

import com.am.socket.dao.ChatMapper;
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
    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Resource
    private UserMapper userMapper;
    @Resource
    private ChatMapper chatMapper;

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
        chatMapper.insertMessageIntoOfflineMessage(offlineMessage);
        log.info("*************** store offline message successfully!");
        return "sent offline message successfully!";
    }

    public List<String> getOfflineMessage(String receiverName) {
        int receiverId = userMapper.findUserFromAccount(receiverName).getId();
        List<OfflineMessage> offlineMessages = chatMapper.findMessageFromOfflineMessage(receiverId, OfflineMessage.NOT_RECEIVED);
        List<String> sendMessage = new ArrayList<>();
        for (OfflineMessage message : offlineMessages) {
            String senderName = userMapper.fineUserIdFromAccount(message.getSenderId()).getUsername();
            String messageFormat = senderName + ": " + message.getOfflineMessage();
            sendMessage.add(messageFormat);
            message.setReceiveState(OfflineMessage.RECEIVED);
            Date receivedTime = new Date();
            message.setReceiveTime(receivedTime);
            chatMapper.updateSendStateOfOfflineMessage(message);
        }
        return sendMessage;
    }
}
