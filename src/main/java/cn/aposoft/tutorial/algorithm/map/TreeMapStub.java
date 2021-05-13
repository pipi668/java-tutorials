package cn.aposoft.tutorial.algorithm.map;

import java.util.TreeMap;

public class TreeMapStub {

    static class Person {
        private String name;
        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }
    }

    public static void main(String[] args) {
        TreeMap<Person, Person> map = new TreeMap<>();
        Person p = new Person();
        p.setName("Liuya");
        Person p2 = new Person();
        //  java.lang.ClassCastException: cn.aposoft.tutorial.algorithm.map.TreeMapStub$Person cannot be cast to java.lang.Comparable
        map.put(p, p);
        map.put(p2, p2);
        Person p1 = map.get(p);
        System.out.println(p1.getName());
    }
}
