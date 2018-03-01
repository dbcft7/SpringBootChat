package com.am.socket.Util;

import javax.crypto.Cipher;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;

public class RSA {

    private  static final int MAX_ENCRYPT_BLOCK = 117;
    private static final int MAX_DECRYPT_BLOCK = 128;
    //generator keypair
    public static KeyPair generateKeyPair() throws Exception{
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(1024);
        return keyPairGenerator.generateKeyPair();
    }

    // encrypting by using public key
    public static byte[] encrypt(String string, PublicKey publicKey) throws Exception{
        byte[] content = string.getBytes();
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE,publicKey);
        return cipher.doFinal(content);
    }

    public static byte[] decrypt(byte[] content, PrivateKey privateKey) throws Exception{
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE,privateKey);
        int inputLength = content.length;
        int offset = 0;
        byte[] cache;

        int i = 0;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        while(inputLength - offset > 0) {
            if (inputLength - offset > MAX_DECRYPT_BLOCK) {
                cache = cipher.doFinal(content,offset,MAX_DECRYPT_BLOCK);
            }else{
                cache = cipher.doFinal(content,offset,inputLength-offset);
            }
            out.write(cache, 0, cache.length);
            i++;
            offset = i*MAX_DECRYPT_BLOCK;
        }
        byte[] decrypted = out.toByteArray();
        out.close();
        return decrypted;
    }


}
