package cn.aposoft.tutorial.algorithm.lru;


import java.util.HashMap;
import java.util.Iterator;

/**
 * 最近最久未使用淘汰策略
 * 基于 双向链表 + 哈希表组成，其中双向链表由哈希链表节点构成
 * 封装为 LRU(K, V)
 * 对外提供 get(K)访问数据、put(K, V)更新数据、Iterator()遍历数据
 */
public class LRU<K, V> implements Iterable<K> {

    private Node head;
    private Node tail;
    //记录K-Node映射，便于快速查找目标数据对应节点
    private HashMap<K, Node> map;
    private int maxSize;

    //哈希链表节点类 Node
    private class Node {
        Node pre;
        Node next;
        K k;
        V v;

        //Node对外提供构造方法
        public Node(K k, V v) {
            this.k = k;
            this.v = v;
        }
    }

    //初始化时必须传入最大可用内存容量
    public LRU(int maxSize) {
        this.maxSize = maxSize;
        //HashMap初始容量设置为 maxSize * 4/3，即达到最大可用内存时，HashMap也不会自动扩容浪费空间
        this.map = new HashMap<>(maxSize * 4 / 3);

        head.next = tail;
        tail.pre = head;
    }

    //获取指定数据
    private V get(K key) {
        //判断是否存在对应数据
        if (!map.containsKey(key)) {
            return null;
        }

        //最新访问的数据移动到链表头
        Node node = map.get(key);
        remove(node);
        addFirst(node);
        return node.v;
    }

    //更新旧数据或添加数据
    private void put(K key, V value) {
        //若存在旧数据则删除
        if (map.containsKey(key)) {
            Node node = map.get(key);
            remove(node);
        }

        //新数据对应节点插入链表头
        Node node = new Node(key, value);
        map.put(key, node);
        addFirst(node);

        //判断是否需要淘汰数据
        if (map.size() > maxSize) {
            removeLast();
            //数据节点淘汰后，同时删除map中的映射
            map.remove(node.k);
        }
    }

    //将指定节点插入链表头
    private void addFirst(Node node) {
        Node next = head.next;

        head.next = node;
        node.pre = head;

        node.next = next;
        next.pre = node;
    }

    //从链表中删除指定节点
    private void remove(Node node) {
        Node pre = node.pre;
        Node next = node.next;

        pre.next = next;
        next.pre = pre;

        node.next = null;
        node.pre = null;
    }

    //淘汰数据
    private Node removeLast() {
        //找到最近最久未使用的数据所对应节点
        Node node = tail.pre;

        //淘汰该节点
        remove(node);

        return node;
    }

    //通过迭代器遍历所有数据对应键
    @Override
    public Iterator<K> iterator() {
        return new Iterator<K>() {

            private Node cur = head.next;

            @Override
            public boolean hasNext() {
                return cur != tail;
            }

            @Override
            public K next() {
                Node node = cur;
                cur = cur.next;
                return node.k;
            }

        };
    }
}
