package cn.aposoft.tutorial.curr.hashmap;

import java.util.Iterator;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

public class ConcurrentHashmapIterator {
	public static void main(String[] args) {
		ConcurrentHashMap<String, String> map = new ConcurrentHashMap<>();
		map.put("1", "1");
		map.put("2", "2");
		int i = 11;
		Iterator<Entry<String, String>> iterator = map.entrySet().iterator();
		while (iterator.hasNext()) {
			System.out.println(iterator.next());
			map.put(String.valueOf(i), String.valueOf(i++));
		}
	}
}
