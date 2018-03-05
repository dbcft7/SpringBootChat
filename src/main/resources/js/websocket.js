let socket;
let SPLIT = '&';
let LOGIN = 'login';
let SEND2USER = 'send2User';
let LOGIN_USERS = 'loginUsers';

if (!window.WebSocket) {
    window.WebSocket = window.MozWebSocket;
}
if (window.WebSocket) {
    socket = new WebSocket("ws://127.0.0.1:8080/web-socket");
    socket.onmessage = function(event) {
        console.log('Message received from server: ' + event.data);

        document.getElementById("content").value += event.data + '\n';
    };
    socket.onopen = function(event) {
        console.log('WebSocket connection opened. Ready to send messages.');
    };
    socket.onclose = function(event) {
        console.log('WebSocket connection closed.');
    };
} else {
    alert("你的浏览器不支持 WebSocket！");
}

function clearContent() {
    document.getElementById("content").value = '';
}

function send(message) {
    if (socket.readyState == WebSocket.OPEN) {
        socket.send(message);
    } else {
        alert("连接没有开启");
    }
}

function login() {
    user = {};
    user.username = document.getElementById("username").value;
    user.password = document.getElementById("password").value;
    message = LOGIN + SPLIT + SPLIT + JSON.stringify(user);
    console.log(message);
    send(message);
}

function send2Person() {
    username = document.getElementById("aimUsername").value;
    msg = document.getElementById("message").value;
    message = SEND2USER + SPLIT + username + SPLIT + msg;
    console.log(message);
    send(message);
}

function checkLoginUsers() {
    message = LOGIN_USERS + SPLIT + SPLIT;
    console.log(message);
    send(message);
}