package com.am.socket.util;

import sun.misc.BASE64Decoder;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;

public class RSA {

    private static final int MAX_ENCRYPT_BLOCK = 117;
    private static final int MAX_DECRYPT_BLOCK = 128;

    public static final String publicKeyString = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDneEL13J6R31bjtdKpn6NiwyVC4Tr0cKxQibt3mcZpRP88U3ronjuYgIcEVFISqBdL2k+8BboZX0O0a81/nF3e4dDNwpZ39JLDgHlwYlJNF7NORrxrSnVDByVIE5bN6vzBR/hi/W1/kR+aOJBEDQjL2X3n73hwKxvn6rTs0OR4ewIDAQAB";
    public static final String privateKeyString = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAOd4QvXcnpHfVuO10qmfo2LDJULhOvRwrFCJu3eZxmlE/zxTeuieO5iAhwRUUhKoF0vaT7wFuhlfQ7RrzX+cXd7h0M3Clnf0ksOAeXBiUk0Xs05GvGtKdUMHJUgTls3q/MFH+GL9bX+RH5o4kEQNCMvZfefveHArG+fqtOzQ5Hh7AgMBAAECgYEA4564wkv8x1gIew6vJcztf86FUtpn/j7axKxc5MUcfyKxl+JD6ILJ+jLSEyjUOLKBjj8Vz8EV+6NZ0g982vOqvgcvShKEy3PGVOpkp77kBpJkOYoxPgSjkbDrRr0U1Q9ZFEPiHtQ8zFF+iufAUbCUwapUbj2VA2axDaW16k0iC7ECQQD0u0PkgQXf8Bq204s9j17W2SgWooxqwjFuyN1fCqiMq8QE0R/5q7ppoM8WCcUD8lVafBBYO5uuwdnHrfx3XrApAkEA8iCs/KuxPsMUAQLVWw4eo0H13BnCL8kpPnkS8+YQvF3utUda0q1cyTrwFWh43/snExGV/i15LToCeqw2ZXIoAwJAeQffxLMWT7Bz1bT66J/t3D0a+U68ONP/FHXcHPOtdZxA3cz6jkQidbikkZaVS6VeFtjCUJByJtQRuxHNiM9YOQJAdoND6NDpCr1Vd0E5yT3aBTLt8nOxNs6Rn4CHpFYB7xFTG+v3KpOsjNqKVRAzyf4WnCDJeAYt/9MbD4xne7QqnQJBAPJwJf+eiEO0XlUhZzeD5APle8Ub52mTjAk1rKZ9drfZPGq205dglPRVwmIEKjORWnmgpf5tmUKMiYWBd7vep9U=";


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

    public static PublicKey getPublicKey(String publicKeyString) {
        PublicKey publicKey = null;
        try {
            java.security.spec.X509EncodedKeySpec bobPubKeySpec = new java.security.spec.X509EncodedKeySpec(new BASE64Decoder().decodeBuffer(publicKeyString));
            java.security.KeyFactory keyFactory;
            keyFactory = java.security.KeyFactory.getInstance("RSA");
            publicKey = keyFactory.generatePublic(bobPubKeySpec);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return publicKey;
    }

    public static PrivateKey getPrivateKey(String privateKeyString) {
        PrivateKey privateKey = null;
        PKCS8EncodedKeySpec privateKeyCS8;
        try {
            privateKeyCS8 = new PKCS8EncodedKeySpec(new BASE64Decoder().decodeBuffer(privateKeyString));
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            privateKey = keyFactory.generatePrivate(privateKeyCS8);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return privateKey;
    }

}
