/**
 *   Copyright  :  www.aposoft.cn
 */
package cn.aposoft.tutorial.lang.character;

import java.nio.charset.Charset;

/**
 * @author LiuJian
 * @date 2017年3月13日
 * 
 */
public class CharacterFilterApp {

    private static final char MAX_CJK_CHAR = '\u9FBF';

    private static final char MAX_CHAR = '\uFFFF';

    /**
     * @param args
     */
    public static void main(String[] args) {
        String s = String.valueOf(MAX_CJK_CHAR);

        byte[] bytes = s.getBytes(Charset.forName("UTF-8"));
        System.out.println((int) MAX_CJK_CHAR + s + bytes.length);
       
        
        String sMax = String.valueOf(MAX_CHAR);
        byte[] bytesMax = sMax.getBytes(Charset.forName("UTF-8"));
        System.out.println(((int) MAX_CHAR) +","+ sMax +","+ bytesMax.length);

        String s1;
        char begin = MAX_CJK_CHAR;

        /*
         * for (char c = begin; c < 65535; c++) {
         * 
         * s1 = String.valueOf(c); byte[] bytes1 =
         * s1.getBytes(Charset.forName("UTF-8")); System.out.println(((int) c) +
         * "," + s1 + bytes1.length); }
         */
    }

}
