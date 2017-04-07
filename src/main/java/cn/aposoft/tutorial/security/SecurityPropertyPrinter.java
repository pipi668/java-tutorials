/**
 *   Copyright  :  www.aposoft.cn
 */
package cn.aposoft.tutorial.security;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

/**
 * @author LiuJian
 * @date 2017年4月5日
 * 
 */
public class SecurityPropertyPrinter {

    private static final String CRLF = "\r\n";

    /**
     * @param args
     */
    public static void main(String[] args) {
        Map<String, String> env = System.getenv();
        System.out.println("CRLF equals ineSeparator:\t\t" + CRLF.equals(System.lineSeparator()));
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println("system.env:\t\t" + env.size());
        for (Entry<String, String> entry : env.entrySet()) {
            System.out.println(entry.getKey() + "\t\t" + entry.getValue());
        }
        System.out.println();
        System.out.println();
        System.out.println();

        Properties props = System.getProperties();
        System.out.println("system.properties:\t\t" + props.size());
        for (Entry<Object, Object> entry : props.entrySet()) {
            System.out.println(entry.getKey() + "\t\t" + entry.getValue());
        }

    }

}
