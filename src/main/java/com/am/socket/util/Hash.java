package com.am.socket.util;

import java.security.MessageDigest;

public class Hash {
    private static final char[] HEXES = {
            '0', '1', '2', '3',
            '4', '5', '6', '7',
            '8', '9', 'a', 'b',
            'c', 'd', 'e', 'f'
    };

    public static String encrypt(String data) throws Exception {
        byte[] content = data.getBytes();
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");
        byte[] cipher = messageDigest.digest(content);
        return byte2Hex(cipher);
    }

    public static String byte2Hex(byte[] bytes){
        if(bytes == null || bytes.length ==0) return null;
        StringBuilder sb = new StringBuilder();
        for(byte b : bytes){
            sb.append(HEXES[(b >> 4) & 0x0F]);
            sb.append(HEXES[b & 0x0F]);
        }
        return sb.toString();
    }

}
