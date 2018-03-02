package com.am.socket.service;

import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.*;

public class WebSocketService extends TextWebSocketHandler {

    private List<WebSocketSession> sessionlist = new ArrayList<>();
    private String[] userIdList = new String[] {"1","2","3"};
    private Map<String,WebSocketSession> map = new HashMap<>();

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
        //session.sendMessage(new TextMessage(": You are now connected to the server. This is the first message."));
        sessionlist.add(session);
        for(String s : userIdList){
            if(!map.containsKey(s)){
                map.put(s,session);
                session.sendMessage(new TextMessage(s + ": You are now connected to the server. This is the first message."));
                break;
            }
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage textMessage) throws Exception {
        /*
        for(WebSocketSession se:sessionlist){
            if(!se.getId().equals(session.getId())){
                System.out.println("Message received from: " + textMessage.getPayload());
                se.sendMessage(textMessage);
            }
        }
        */
        System.out.println("Message received from: " + textMessage.getPayload());
        String[] massage = textMessage.getPayload().split(":");
        System.out.println("massage sender is: ****************  " + massage[0]);
        for(String s: userIdList){
            if(s.equals(massage[0])){
                WebSocketSession sendSession = map.get(massage[0]);
                sendSession.sendMessage(textMessage);
                break;
            }
        }
    }
    
}