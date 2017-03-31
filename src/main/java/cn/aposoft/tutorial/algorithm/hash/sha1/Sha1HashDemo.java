/**
 *   Copyright  :  www.aposoft.cn
 */
package cn.aposoft.tutorial.algorithm.hash.sha1;

import java.nio.charset.Charset;

import org.apache.commons.codec.digest.DigestUtils;

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
        System.out.println(DigestUtils.sha1Hex(orgin.getBytes(Charset.forName("UTF-8"))));
    }

}
