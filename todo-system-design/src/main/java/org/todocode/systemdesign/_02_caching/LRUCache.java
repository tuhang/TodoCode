package org.todocode.systemdesign._02_caching;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * [TodoCode] LRU 缓存实现
 *
 * <h3>背景:</h3>
 * LRU (最近最少使用) 是最常见的缓存淘汰策略。
 * 理解其实现有助于分布式缓存设计。
 *
 * <h3>易错点:</h3>
 * 缓存失效是计算机科学中最难的问题之一。
 * 始终考虑 TTL 和一致性要求。
 *
 * <h3>核心理解:</h3>
 * 使用 accessOrder=true 的 LinkedHashMap 非常适合 LRU。
 * 生产环境建议使用 Caffeine 或 Redis，而不是自己实现。
 */
public class LRUCache<K, V> extends LinkedHashMap<K, V> {

    private final int capacity;

    public LRUCache(int capacity) {
        super(capacity, 0.75f, true); // true = 访问顺序
        this.capacity = capacity;
    }

    @Override
    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        return size() > capacity;
    }

    /**
     * 使用同步的线程安全版本。
     * 对于高并发，考虑使用 ConcurrentLinkedHashMap 或 Caffeine。
     */
    public static class ThreadSafeLRUCache<K, V> {
        private final LRUCache<K, V> cache;

        public ThreadSafeLRUCache(int capacity) {
            this.cache = new LRUCache<>(capacity);
        }

        public synchronized V get(K key) {
            return cache.get(key);
        }

        public synchronized void put(K key, V value) {
            cache.put(key, value);
        }

        public synchronized V remove(K key) {
            return cache.remove(key);
        }

        public synchronized int size() {
            return cache.size();
        }
    }
}
