/**
 * Copyright  :  www.aposoft.cn
 */
package cn.aposoft.tutorial.algorithm.hash.sha1;

import org.apache.commons.codec.digest.DigestUtils;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;


/**
 * @author LiuJian
 * @date 2017年3月21日
 *
 */
public class Sha1HashDemo {

    /**
     * @param args
     */
    public static void main(String[] args) {
        final String orgin = "originstring";
        System.out.println(DigestUtils.sha1Hex(orgin.getBytes(StandardCharsets.US_ASCII)));
    }
}
