package cn.aposoft.tutorial.files.random;

import java.util.Random;

public class FileSaveToolBootstrap {
    static Random r = new Random();

    public static void main(String[] args) {
        String basePath = "d:/data/file/";
        String dateMonth = "202104";
        String randomPath = String.valueOf(99999 + r.nextInt(Integer.MAX_VALUE - 100000));
        String filePath = basePath + dateMonth + "/" + randomPath;

    }
}