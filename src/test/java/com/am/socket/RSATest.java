package com.am.socket;

import com.am.socket.util.Hash;
import com.am.socket.util.RSA;
import org.junit.Test;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;

import static com.am.socket.util.RSA.decrypt;
import static com.am.socket.util.RSA.encrypt;
import static com.am.socket.util.RSA.generateKeyPair;

//@RunWith(SpringRunner.class)
//@SpringBootTest(classes = Application.class)
public class RSATest {
    private String string = "Hello world!";

    @Test
    public void RSR() throws Exception {
        PublicKey publicKey = RSA.getPublicKey(RSA.publicKeyString);
        PrivateKey privateKey = RSA.getPrivateKey(RSA.privateKeyString);
        System.out.println("public key is:  "+ new String(Base64.getEncoder().encode(publicKey.getEncoded())));
        System.out.println("private key is:  "+new String(Base64.getEncoder().encode(privateKey.getEncoded())));
        byte[] encrypted = encrypt(string, publicKey);
        System.out.println("after encrypted:  " + new String(encrypted));
        String  decrypted = new String(decrypt(encrypted, privateKey));
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
    public void Hash() throws Exception{
        String data = "123456";
        System.out.println("original data is: " + data);
        String hash = Hash.encrypt(data);
        System.out.println("after Hashed: " + hash);
    }
}
