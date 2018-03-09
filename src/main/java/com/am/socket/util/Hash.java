package com.am.socket.util;

import java.security.MessageDigest;
import java.util.Random;

public class Hash {
    private static final char[] HEXES = {
            '0', '1', '2', '3',
            '4', '5', '6', '7',
            '8', '9', 'a', 'b',
            'c', 'd', 'e', 'f'
    };

    public static String encrypt(String data, String salt) throws Exception {
        byte[] password = (data + salt).getBytes();
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");
        byte[] cipher = messageDigest.digest(password);
        return byte2Hex(cipher);
    }

    private static String byte2Hex(byte[] bytes){
        if(bytes == null || bytes.length ==0) return null;
        StringBuilder sb = new StringBuilder();
        for(byte b : bytes){
            sb.append(HEXES[(b >> 4) & 0x0F]);
            sb.append(HEXES[b & 0x0F]);
        }
        return sb.toString();
    }

    public static String generateSalt() {
        Random r = new Random();
        StringBuilder sb = new StringBuilder(16);
        sb.append(r.nextInt(99999999)).append(r.nextInt(99999999));
        if (sb.length() < 16) {
            for (int i = 0; i < 16 - sb.length(); i++) {
                sb.append(0);
            }
        }
        return sb.toString();
    }

}
