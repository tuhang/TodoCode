package org.todocode.concurrency._05_collections;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * [TodoCode] 并发集合演示
 *
 * <h3>背景:</h3>
 * 标准集合 (ArrayList, HashMap) 不是线程安全的。
 * JUC 提供了具有不同权衡的线程安全替代方案。
 *
 * <h3>易错点:</h3>
 * ConcurrentHashMap.size() 和 isEmpty() 可能是过时的!
 * 不要依赖它们做关键决策。
 *
 * <h3>核心理解:</h3>
 * - ConcurrentHashMap: 高并发，分段锁
 * - CopyOnWriteArrayList: 读多写少场景
 * - Collections.synchronizedXxx: 简单包装，单一锁(慢)
 */
public class ConcurrentCollectionsDemo {

    /**
     * ConcurrentHashMap 演示: 安全的并发修改。
     *
     * TODO: 与 Collections.synchronizedMap() 比较性能
     */
    public void concurrentHashMapDemo() {
        ConcurrentHashMap<String, Integer> map = new ConcurrentHashMap<>();

        // 原子操作
        map.put("counter", 0);

        // compute() 是原子的 - 不需要外部同步
        map.compute("counter", (key, value) -> value == null ? 1 : value + 1);

        // putIfAbsent() - 仅当 key 不存在时才放入
        map.putIfAbsent("counter", 100); // 不会改变，因为 key 已存在

        // computeIfAbsent() - 仅当 key 不存在时才计算
        map.computeIfAbsent("newKey", key -> key.length());

        System.out.println("counter: " + map.get("counter"));
        System.out.println("newKey: " + map.get("newKey"));

        // 安全迭代 - 不会抛出 ConcurrentModificationException
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            System.out.println(entry.getKey() + " = " + entry.getValue());
        }
    }

    /**
     * CopyOnWriteArrayList 演示: 最适合读多写少场景。
     *
     * TODO: 为什么 CopyOnWriteArrayList 对写密集工作负载很糟糕？
     */
    public void copyOnWriteDemo() {
        CopyOnWriteArrayList<String> list = new CopyOnWriteArrayList<>();

        list.add("item1");
        list.add("item2");
        list.add("item3");

        // 即使在修改期间迭代也是安全的
        // 迭代器看到的是列表的快照
        for (String item : list) {
            System.out.println(item);
            if (item.equals("item2")) {
                list.add("item4"); // 这不会影响当前迭代
            }
        }

        System.out.println("最终列表: " + list);
    }

    /**
     * 演示 ConcurrentHashMap 中的原子复合操作。
     * 这些对于线程安全的计数器和缓存至关重要。
     */
    public void atomicOperationsDemo() {
        ConcurrentHashMap<String, Long> counters = new ConcurrentHashMap<>();

        // 安全的自增模式
        String key = "pageViews";
        counters.merge(key, 1L, Long::sum); // 初始化为 1 或自增
        counters.merge(key, 1L, Long::sum);
        counters.merge(key, 1L, Long::sum);

        System.out.println(key + ": " + counters.get(key)); // 应该是 3

        // 使用 computeIfAbsent 的安全缓存模式
        ConcurrentHashMap<String, String> cache = new ConcurrentHashMap<>();
        String value = cache.computeIfAbsent("user:123", k -> {
            System.out.println("为 " + k + " 计算值");
            return "UserData"; // 模拟昂贵的计算
        });
        System.out.println("第一次调用: " + value);

        // 第二次调用 - 不会计算
        value = cache.computeIfAbsent("user:123", k -> {
            System.out.println("这不会被调用");
            return "NewData";
        });
        System.out.println("第二次调用: " + value);
    }
}
