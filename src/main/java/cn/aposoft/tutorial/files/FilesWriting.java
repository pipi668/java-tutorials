package cn.aposoft.tutorial.files;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Date;

public class FilesWriting {

    public static void main(String[] args) throws IOException {
        File file = new File("d:/a.txt");

        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8));) {
            String s = "if (redis.call('hexists', KEYS[1], ARGV[3]) == 0) then " +
                    "return nil;" +
                    "end; " +
                    "local counter = redis.call('hincrby', KEYS[1], ARGV[3], -1); " +
                    "if (counter > 0) then " +
                    "redis.call('pexpire', KEYS[1], ARGV[2]); " +
                    "return 0; " +
                    "else " +
                    "redis.call('del', KEYS[1]); " +
                    "redis.call('publish', KEYS[2], ARGV[1]); " +
                    "return 1; " +
                    "end; " +
                    "return nil;\n";
            System.out.println(new Date());
            for (int i = 0; i < 1000 * 1000; i++) {
                writer.append(s);
            }
            writer.flush();
            System.out.println(new Date());
        }
    }


}
