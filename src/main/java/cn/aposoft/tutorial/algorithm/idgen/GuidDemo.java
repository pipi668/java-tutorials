package cn.aposoft.tutorial.algorithm.idgen;

import java.util.UUID;

public class GuidDemo {
    public static void main(String[] args) {

        UUID id = UUID.randomUUID();
        System.out.println(id.toString());

        System.out.println(id.getMostSignificantBits());
        System.out.println(id.getLeastSignificantBits());
        // only version 1 supported
        //  id.timestamp();
    }
}
