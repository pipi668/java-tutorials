/**
 *   Copyright  :  www.aposoft.cn
 */
package cn.aposoft.tutorial.http.https.store;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.util.Enumeration;

/**
 * @author LiuJian
 * @date 2017年4月18日
 * 
 */
public class KeyStoreSave {

    /**
     * @param args
     * @throws KeyStoreException
     * @throws IOException
     * @throws CertificateException
     * @throws NoSuchAlgorithmException
     */
    public static void main(String[] args) throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException {
        final String keyFileName = "jssecacerts", password = "changeit", keyStoreType = "JKS";

        final String officialKeyStoreName = "StartComRoot1.jks";
        File file = new File(keyFileName);
        char[] passphrase = password.toCharArray();
        System.out.println("Loading KeyStore " + file + "...");

        try (InputStream in = new FileInputStream(file);) {
            KeyStore ks = KeyStore.getInstance(keyStoreType);
            ks.load(in, passphrase);
            Enumeration<String> enu = ks.aliases();

            while (enu.hasMoreElements()) {
                // System.out.println(enu.nextElement());
                String alias = enu.nextElement();
                if ("startcom_certification_authority".equals(alias)) {
                    Certificate cert = ks.getCertificate("startcom_certification_authority");
                    System.out.println(cert);
                } else {
                    ks.deleteEntry(alias);
                }
            }

            try (OutputStream stream = new FileOutputStream(officialKeyStoreName);) {
                ks.store(stream, password.toCharArray());
            }
        }

    }

}
