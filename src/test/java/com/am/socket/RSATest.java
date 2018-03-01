package com.am.socket;

import com.am.socket.Util.Hash;
import com.am.socket.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;

import static com.am.socket.Util.RSA.decrypt;
import static com.am.socket.Util.RSA.encrypt;
import static com.am.socket.Util.RSA.generateKeyPair;
import static com.am.socket.Util.Hash.byte2Hex;

//@RunWith(SpringRunner.class)
//@SpringBootTest(classes = Application.class)
public class RSATest {
    private String string = "Hello world!";


    @Test
    public void RSR()throws Exception{
        KeyPair keyPair = generateKeyPair();
        PublicKey publicKey = keyPair.getPublic();
        PrivateKey privateKey = keyPair.getPrivate();
        System.out.println("public key is:  "+ new String(Base64.getEncoder().encode(publicKey.getEncoded())));
        System.out.println("private key is:  "+new String(Base64.getEncoder().encode(privateKey.getEncoded())));
        byte[]  encrypted = encrypt(string,publicKey);
        //String en = byte2Hex(encrypted);
        System.out.println("after encrypted:  " + new String(encrypted));
        //System.out.println("********after encrypted:  " + en);

        String  decrypted = new String(decrypt(encrypted,privateKey));
        System.out.println("after decrypted:  " + decrypted);
    }

    @Test
    public void Hash() throws Exception{
        String data = "123456";
        System.out.println("original data is: " + data);
        String hash = Hash.encrypt(data);
        System.out.println("after Hashed: " + hash);
    }
}
