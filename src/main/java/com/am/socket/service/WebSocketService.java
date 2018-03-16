package com.am.socket.service;

import com.alibaba.fastjson.JSON;
import com.am.socket.model.User;
import com.am.socket.util.RSA;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import javax.annotation.Resource;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import static com.am.socket.util.RSA.decrypt;
import static com.am.socket.util.RSA.getPrivateKey;

public class WebSocketService extends TextWebSocketHandler {

    private Map<String,WebSocketSession> map = new HashMap<>();

    private static final String SPLIT = "&";
    private static final String LOGIN = "login";
    private static final String SEND2USER = "send2User";
    private static final String LOGIN_USERS = "loginUsers";

    @Resource
    private UserService user;

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        for (String key : map.keySet()) {
            if (map.get(key).equals(session)) {
                map.remove(key);
                break;
            }
        }
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        //sessionList.add(session);
        session.sendMessage(new TextMessage("You are now connected to the server. This is the first message."));
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage textMessage) throws Exception {
        String[] message = textMessage.getPayload().split(SPLIT);

        if (message[0].equals(LOGIN)) {
            handleLogin(session, message);
        } else if (message[0].equals(SEND2USER)) {
            handleSend2User(session, message);
        } else if (message[0].equals(LOGIN_USERS)){
            handleLoginUsers(session);
        } else {
            session.sendMessage(textMessage);
        }

    }

    private void handleLogin(WebSocketSession session, String[] message) throws Exception {
        User userLogin = JSON.parseObject(message[2],User.class);
        String username = userLogin.getUsername();
        String password = new String(decrypt(userLogin.getPassword(), RSA.getPrivateKey(RSA.privateKeyString)));
        if (!user.userLoginForWebSocket(username, password)) {
            System.out.println("************************** login failed!");
            map.remove(username);
            session.sendMessage(new TextMessage("the username or password is wrong, login failed!"));
        } else {
            System.out.println("*************************"+message[2]);
            if (!map.containsValue(session)) {
                map.put(username, session);
            } else {
                for (String key : map.keySet()) {
                    if (map.get(key).equals(session)) {
                        map.remove(key);
                        map.put(username, session);
                        break;
                    }
                }
            }

            session.sendMessage(new TextMessage("welcome to Chat!"));

            for (WebSocketSession sessionSend : map.values()) {
                handleLoginUsers(sessionSend);
            }
        }
    }

    private void handleSend2User(WebSocketSession session, String[] message) throws IOException {
        if (!map.values().contains(session)){
            session.sendMessage(new TextMessage("You didn't login yet, please login first!"));
        } else {
            if (map.get(message[1]) == null) {
                session.sendMessage(new TextMessage("the user " + message[1] + " is not exist!"));
            } else {
                WebSocketSession sendSession = map.get(message[1]);
                sendSession.sendMessage(new TextMessage(message[2]));
            }
        }
    }

    private void handleLoginUsers(WebSocketSession session) throws Exception {
        if (map.values().contains(session)){
            session.sendMessage(new TextMessage(LOGIN_USERS + SPLIT + SPLIT + map.keySet()));
        } else {
            session.sendMessage((new TextMessage("You didn't login yet, please login first!")));
        }
    }

}