/**
 *   Copyright  :  www.aposoft.cn
 */
package cn.aposoft.tutorial.lang;

/**
 * @author LiuJian
 * @date 2016年9月21日
 * 
 */
public class App {

    /**
     * @param args
     */
    public static void main(String[] args) {

        int i = 0;
        long begin = System.currentTimeMillis(); // 一亿次/s以上 178852376,176083143,177243103
        while (System.currentTimeMillis() - begin < 1000) {
            i++;
        }
        System.out.println(i + "," + i / 1000 / 1000);
    }

}
