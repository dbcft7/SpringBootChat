package com.am.socket.util;

import com.am.socket.util.Hash;
import com.am.socket.util.RSA;
import com.am.socket.util.SendEmail;
import org.junit.Test;
import org.slf4j.LoggerFactory;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;
import java.util.logging.Logger;

import static com.am.socket.util.RSA.decrypt;
import static com.am.socket.util.RSA.encrypt;
import static com.am.socket.util.RSA.generateKeyPair;

//@RunWith(SpringRunner.class)
//@SpringBootTest(classes = Application.class)
public class RSATest {
    private String string = "iloveyou";
    private org.slf4j.Logger log = LoggerFactory.getLogger(this.getClass());

    @Test
    public void RSA() throws Exception {
        PublicKey publicKey = RSA.getPublicKey(RSA.publicKeyString);
        PrivateKey privateKey = RSA.getPrivateKey(RSA.privateKeyString);
        System.out.println("public key is:  "+ new String(Base64.getEncoder().encode(publicKey.getEncoded())));
        System.out.println("private key is:  "+new String(Base64.getEncoder().encode(privateKey.getEncoded())));
        byte[] encrypted = RSA.encrypt(string, publicKey);
        String encryptedString = Base64.getEncoder().encodeToString(encrypted);
        System.out.println("after encrypted:  " + encryptedString);
        String decrypted = new String(RSA.decrypt(encryptedString, privateKey));
        System.out.println("after decrypted:  " + decrypted);
    }

    @Test
    public void generateKeyPair() throws Exception {
        KeyPair keyPair = RSA.generateKeyPair();
        PublicKey publicKey = keyPair.getPublic();
        PrivateKey privateKey = keyPair.getPrivate();
        System.out.println("public key is:  "+ new String(Base64.getEncoder().encode(publicKey.getEncoded())));
        System.out.println("private key is:  "+new String(Base64.getEncoder().encode(privateKey.getEncoded())));
    }

    @Test
    public void hash() throws Exception{
        String data = "12345";
        String salt = Hash.generateSalt();
        System.out.println("original data is: " + data);
        String hash = Hash.encrypt(data=salt);
        System.out.println("after Hashed: " + hash);
    }

    @Test
    public void sendEmail() {
        SendEmail.send("496514152@qq.com", "hello!");
    }

    @Test
    public void testLog() {
        log.info("****************Test Log******************");

    }

}
