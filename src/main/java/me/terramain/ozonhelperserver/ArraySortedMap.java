package me.terramain.ozonhelperserver;

import java.util.*;

public class ArraySortedMap<K, V> {
    private final List<Map.Entry<K,V>> list;

    public ArraySortedMap() {
        this.list = new ArrayList<>();
    }

    public int size() {
        return list.size();
    }

    public boolean isEmpty() {
        return size()==0;
    }

    public boolean containsKey(Object key) {
        for (Map.Entry<K, V> entry : list) {
            if (entry.getKey().equals(key)){
                return true;
            }
        }
        return false;
    }

    public boolean containsValue(Object value) {
        for (Map.Entry<K, V> entry : list) {
            if (entry.getValue().equals(value)){
                return true;
            }
        }
        return false;
    }

    public V get(Object key) {
        for (Map.Entry<K, V> entry : list) {
            if (entry.getKey().equals(key)){
                return entry.getValue();
            }
        }
        return null;
    }

    public V put(K key, V value) {
        for (Map.Entry<K, V> entry : list) {
            if (entry.getKey().equals(key)){
                return entry.setValue(value);
            }
        }
        Map.Entry<K, V> entry = new ArrayEntry<>(key, value);
        list.add(entry);
        return entry.getValue();
    }

    public V remove(Object key) {
        for (Map.Entry<K, V> entry : list) {
            if (entry.getKey().equals(key)){
                list.remove(entry);
                return entry.getValue();
            }
        }
        return null;
    }

    public void putAll(Map<? extends K, ? extends V> m) {
        m.forEach(this::put);
    }

    public void clear() {
        list.clear();
    }

    public Set<K> keySet() {
        Set<K> set = new HashSet<>();
        list.forEach(kvEntry -> {
            set.add(kvEntry.getKey());
        });
        return set;
    }

    public Collection<V> values() {
        Set<V> set = new HashSet<>();
        list.forEach(kvEntry -> {
            set.add(kvEntry.getValue());
        });
        return set;
    }

    public Set<Map.Entry<K, V>> entrySet() {
        return new HashSet<>(list);
    }

    public static class ArrayEntry<K, V> implements Map.Entry<K, V> {
        private final K key;
        private V value;

        public ArrayEntry(K key, V value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public K getKey() {
            return key;
        }

        @Override
        public V getValue() {
            return value;
        }

        @Override
        public V setValue(V value) {
            this.value = value;
            return value;
        }
    }
    public List<Map.Entry<K,V>> entryList(){
        return list;
    }
    public V getByIndex(int index){
        return list.get(index).getValue();
    }
}
