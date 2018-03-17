let socket;
let SPLIT = '&';
let LOGIN = 'login';
let SEND2USER = 'send2User';
let LOGIN_USERS = 'loginUsers';
let URL = '127.0.0.1:8080';
let HTTP_URL = 'http://127.0.0.1';
let WS_URL = 'ws://127.0.0.1:8080';

let uuid = '';

if (!window.WebSocket) {
    window.WebSocket = window.MozWebSocket;
}
if (window.WebSocket) {
    socket = new WebSocket(WS_URL + "/web-socket");
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
getCaptcha();


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

function guid() {
    function S4() {
        return (((1 + Math.random()) * 0x10000) | 0).toString(16).substring(1);
    }
    return (S4() + S4() + "-" + S4() + "-" + S4() + "-" + S4() + "-" + S4() + S4() + S4());
}

function getCaptcha() {
    uuid = guid();
    let xmlhttp = new XMLHttpRequest();
    xmlhttp.open("GET", "/user/captcha?uuid=" + uuid, true);
    xmlhttp.responseType = "blob";
    xmlhttp.onload = function() {
        if (this.status == 200) {
            let blob = this.response;
            let img = document.getElementById("captchaImg");
            img.src = window.URL.createObjectURL(blob);
        }
    }
    xmlhttp.send();
}

function loginHttp() {
    username = document.getElementById("username").value;
    password = document.getElementById("password").value;
    captcha = document.getElementById("captcha").value;
    password = URLencode(encrypt(password));

    let xmlhttp = new XMLHttpRequest();
    requestUrl = '/user/login?username=' + username 
                    + '&password=' + password 
                    + '&captcha=' + captcha 
                    + '&uuid=' + uuid;
    xmlhttp.open("GET", requestUrl, true);
    xmlhttp.responseType = "blob";
    xmlhttp.onload = function() {
        if (this.status == 200) {
            console.log(this);
        }
    }
    xmlhttp.send();
}

function URLencode(sStr) {
    return escape(sStr).replace(/\+/g, '%2B').replace(/\"/g,'%22').replace(/\'/g, '%27').replace(/\//g,'%2F');  
}