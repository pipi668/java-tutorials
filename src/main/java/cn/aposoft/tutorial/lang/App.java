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
        Object o = new Object();
        synchronized (o) {
            try {
                o.wait(60 * 1000 * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
