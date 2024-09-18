package me.terramain.ozonhelperserver;

import java.util.*;

public class ArraySortedMap<K, V> implements Map<K, V> {
    private List<Map.Entry<K,V>> list;

    public ArraySortedMap() {
        this.list = new ArrayList<>();
    }

    @Override
    public int size() {
        return list.size();
    }

    @Override
    public boolean isEmpty() {
        return size()==0;
    }

    @Override
    public boolean containsKey(Object key) {
        for (Entry<K, V> entry : list) {
            if (entry.getKey().equals(key)){
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean containsValue(Object value) {
        for (Entry<K, V> entry : list) {
            if (entry.getValue().equals(value)){
                return true;
            }
        }
        return false;
    }

    @Override
    public V get(Object key) {
        for (Entry<K, V> entry : list) {
            if (entry.getKey().equals(key)){
                return entry.getValue();
            }
        }
        return null;
    }

    @Override
    public V put(K key, V value) {
        for (Entry<K, V> entry : list) {
            if (entry.getKey().equals(key)){
                return entry.setValue(value);
            }
        }
        Entry<K, V> entry = new ArrayEntry<>(key, value);
        list.add(entry);
        return entry.getValue();
    }

    @Override
    public V remove(Object key) {
        for (Entry<K, V> entry : list) {
            if (entry.getKey().equals(key)){
                list.remove(entry);
                return entry.getValue();
            }
        }
        return null;
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        m.forEach(this::put);
    }

    @Override
    public void clear() {
        list.clear();
    }

    @Override
    public Set<K> keySet() {
        Set<K> set = new HashSet<>();
        list.forEach(kvEntry -> {
            set.add(kvEntry.getKey());
        });
        return set;
    }

    @Override
    public Collection<V> values() {
        Set<V> set = new HashSet<>();
        list.forEach(kvEntry -> {
            set.add(kvEntry.getValue());
        });
        return set;
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        return new HashSet<>(list);
    }

    public static class ArrayEntry<K, V> implements Entry<K, V> {
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
    public V getByIndex(int index){
        return list.get(index).getValue();
    }
}
