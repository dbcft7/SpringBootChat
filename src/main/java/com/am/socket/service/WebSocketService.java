package com.am.socket.service;

import com.alibaba.fastjson.JSON;
import com.am.socket.dao.UserMapper;
import com.am.socket.model.User;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import javax.annotation.Resource;
import java.util.*;

public class WebSocketService extends TextWebSocketHandler {

    private List<WebSocketSession> sessionlist = new ArrayList<>();
    private String[] userIdList = new String[] {"1","2","3"};
    private Map<String,WebSocketSession> map = new HashMap<>();

    @Resource
    private UserService user;

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        for(String key : map.keySet()){
            if(map.get(key).equals(session)){
                map.remove(key);
                break;
            }
        }
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessionlist.add(session);
        //session.sendMessage(new TextMessage(+ ": You are now connected to the server. This is the first message."));
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage textMessage) throws Exception {
        //System.out.println("Message received from: " + textMessage.getPayload());
        String[] massage = textMessage.getPayload().split("$");
        if(massage[0].equals("login")){
            System.out.println("*************************"+massage[2]);
            User userLogin = JSON.parseObject(massage[2],User.class);
            String username = userLogin.getUsername();
            String password = userLogin.getUsername();
            if(!handleLogin(username,password)) session.close();
            else{
                map.put(username,session);
            }
        }
        else if(massage[0].equals("send2User")){
            for(String s: userIdList) {
                if (s.equals(massage[1])) {
                    WebSocketSession sendSession = map.get(massage[1]);
                    sendSession.sendMessage(textMessage);
                    break;
                }
            }
        }
        else{
            session.sendMessage(textMessage);
        }

    }

    private boolean handleLogin(String username, String password) throws Exception {
        return user.findUserIsTrue(username,password);
    }
    
}