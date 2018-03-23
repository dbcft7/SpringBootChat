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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;

import static com.am.socket.util.RSA.decrypt;

@Service
public class WebSocketService extends TextWebSocketHandler {

    private Map<String,WebSocketSession> map = new HashMap<>();
    private Map<WebSocketSession, String> session2user = new HashMap<>();

    private static final String SPLIT = "&";
    private static final String LOGIN = "login";
    private static final String SEND2USER = "send2User";
    private static final String LOGIN_USERS = "loginUsers";
    private static final String OFFLINE_MESSAGE = "offline";
    private static final int NOTRECEIVED = 0;
    private static final int RECEIVED = 1;

    @Resource
    private UserService userService;

    @Resource
    private ChatService chatService;

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
        } else if (message[0].equals(OFFLINE_MESSAGE)) {
            handleOfflineMessage(session, message);
        } else {
            session.sendMessage(textMessage);
        }

    }

    //login::{"userName":"xxx", "password:":"xxxxx"}
    private void handleLogin(WebSocketSession session, String[] message) throws Exception {
        User userLogin = JSON.parseObject(message[2],User.class);
        String username = userLogin.getUsername();
        String password = new String(decrypt(userLogin.getPassword(), RSA.getPrivateKey(RSA.privateKeyString)));
        if (!userService.userLoginForWebSocket(username, password)) {
            System.out.println("************************** login failed!");
            map.remove(username);
            session.sendMessage(new TextMessage("the username or password is wrong, login failed!"));
        } else {
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
            List<String> offlineMessages = chatService.getOfflineMessage(username);
            if (offlineMessages != null && offlineMessages.size() != 0){
                for (String string : offlineMessages) {
                    session.sendMessage(new TextMessage(string));
                    System.out.println(string);
                }
            }
        }
    }

    //send2User:userName:xxxxxxxxxxx:datetime
    private void handleSend2User(WebSocketSession session, String[] message) throws IOException {
        if (!map.containsValue(session)) {
            session.sendMessage(new TextMessage("You didn't login yet, please login first!"));
        } else {
            if (!userService.findUserFromDB(message[1])) {
                session.sendMessage(new TextMessage("the user you want to send message to is not exist!"));
            } else if (map.get(message[1]) == null) {
                session.sendMessage(new TextMessage("the user " + message[1] + " is not online now, please send the offline message!"));
            } else {
                String senderName = "";
                for (String key : map.keySet()) {
                    if (map.get(key).equals(session)) {
                        senderName = key;
                        break;
                    }
                }

                WebSocketSession sendSession = map.get(message[1]);
                sendSession.sendMessage(new TextMessage(senderName + ": " + message[2]));
            }
        }
    }


    private void handleLoginUsers(WebSocketSession session) throws Exception {
        if (map.containsValue(session)){
            session.sendMessage(new TextMessage(LOGIN_USERS + SPLIT + SPLIT + map.keySet()));
        } else {
            session.sendMessage(new TextMessage("You didn't login yet, please login first!"));
        }
    }

    //offline&receiverName&offlineMessage&datetime
    private void handleOfflineMessage(WebSocketSession session, String[] message) throws IOException {
        String senderName = "";
        for (String key : map.keySet()) {
            if (map.get(key).equals(session)) {
                senderName = key;
                break;
            }
        }
        if (map.containsKey(message[1])) {
            WebSocketSession sendSession = map.get(message[1]);
            sendSession.sendMessage(new TextMessage(senderName + ": " + message[2]));
            String send = chatService.storeOfflineMessage(senderName, message[1], message[2], message[3], RECEIVED);
            session.sendMessage(new TextMessage(send));
        } else {
            String send = chatService.storeOfflineMessage(senderName, message[1], message[2], message[3], NOTRECEIVED);
            session.sendMessage(new TextMessage(send));
        }
    }

}