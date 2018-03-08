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
    user.password = encrypt(user.password);
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

function encrypt(msg) {
    let publicKeyString = '-----BEGIN PUBLIC KEY-----\n' +
        'MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDneEL13J6R31bjtdKpn6NiwyVC\n' +
        '4Tr0cKxQibt3mcZpRP88U3ronjuYgIcEVFISqBdL2k+8BboZX0O0a81/nF3e4dDN\n' +
        'wpZ39JLDgHlwYlJNF7NORrxrSnVDByVIE5bN6vzBR/hi/W1/kR+aOJBEDQjL2X3n\n' +
        '73hwKxvn6rTs0OR4ewIDAQAB\n' +
        '-----END PUBLIC KEY-----';
    var encrypt = new JSEncrypt();
    encrypt.setPublicKey(publicKeyString);
    var encrypted = encrypt.encrypt(msg);
    return encrypted;
}