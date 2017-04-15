/**
 *   Copyright  :  www.aposoft.cn
 */
package cn.aposoft.tutorial.http.https.certs;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

/**
 * @author LiuJian
 * @date 2017年3月28日
 * 
 */
public class CaReader {

    /**
     * 读取JKS Keystore格式文件
     * 
     * @param args
     * @throws KeyStoreException
     * @throws IOException
     * @throws FileNotFoundException
     * @throws CertificateException
     * @throws NoSuchAlgorithmException
     */
    public static void main(String[] args)
            throws KeyStoreException, NoSuchAlgorithmException, CertificateException, FileNotFoundException, IOException {
        File file = new File("jssecacerts");
        final String password = "changeit";
        KeyStore truststoreSrc = KeyStore.getInstance("JKS");
        truststoreSrc.load(new FileInputStream(file), password.toCharArray());
        
    }

}
