/**
 *   Copyright  :  www.aposoft.cn
 */
package cn.aposoft.tutorial.character;

/**
 * @author LiuJian
 * @date 2017年3月13日
 * 
 */
public class EmojiTest {

    /**
     * @param args
     */
    public static void main(String[] args) {
        char[] ca = new char[10];
        ca[0] = '\uD800'; // 1
        ca[1] = '\uDC00';// 2
        ca[2] = 'A';
        ca[3] = '司';
        ca[4] = '\uD801';// 3
        ca[5] = '王';
        ca[6] = '\uDFFF';// 4
        ca[7] = '\uDBFF';// 5
        ca[8] = '\uDC00';// 6
        ca[9] = '\u0000';
        String s = String.valueOf(ca);
        System.out.println(s.length() + "," + s);
        String r = EmojiCharacterUtil.filterEmoji(s);

        System.out.println(r.length() + "," + r);
    }

}
