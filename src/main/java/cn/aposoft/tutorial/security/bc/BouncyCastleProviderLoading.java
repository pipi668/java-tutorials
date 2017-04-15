/**
 *   Copyright  :  www.aposoft.cn
 */
package cn.aposoft.tutorial.security.bc;

import java.security.Security;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

/**
 * @author LiuJian
 * @date 2017年4月15日
 * 
 */
public class BouncyCastleProviderLoading {

    /**
     * @param args
     */
    public static void main(String[] args) {
        // 动态添加 BouncyCastleProvider.
        Security.addProvider(new BouncyCastleProvider());
        // another way is to add entry to the java security properties file.
        // D:\devbin\Java\jdk1.7.0_80\jre\lib\security\java.security添加
        // security.provider.<n>=org.bouncycastle.jce.provider.BouncyCastleProvider

        // clean room implementation of the JCE API
        // light-weight cryptographic API consisting of support for
        // BlockCipher
        // BufferedBlockCipher
        // AsymmetricBlockCipher
        // BufferedAsymmetricBlockCipher
        // StreamCipher
        // BufferedStreamCipher
        // KeyAgreement
        // IESCipher
        // Digest
        // Mac
        // PBE
        // Signers
        // JCE compatible framework for a Bouncy Castle provider "BC".
        // JCE compatible framework for a Bouncy Castle post-quantum provider
        // "BCPQC".

    }

}
