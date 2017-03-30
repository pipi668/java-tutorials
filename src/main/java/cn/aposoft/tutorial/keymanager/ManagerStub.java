package cn.aposoft.tutorial.keymanager;

import java.security.NoSuchAlgorithmException;

import javax.net.ssl.KeyManagerFactory;

public class ManagerStub {
    public static void main(String[] args) throws NoSuchAlgorithmException {
        String algorithm = KeyManagerFactory.getDefaultAlgorithm();
        System.out.println(algorithm);
        KeyManagerFactory factory = KeyManagerFactory.getInstance(algorithm);
        
        
        
    }
}
