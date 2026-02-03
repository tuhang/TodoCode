package org.todocode.concurrency._02_safety;

import java.util.concurrent.TimeUnit;

/**
 * [TodoCode] Volatile 可见性演示
 *
 * <h3>背景:</h3>
 * Volatile 只保证可见性，不保证原子性。
 * 就像在群里发消息（可见性），
 * 但你无法阻止别人同时插话（原子性）。
 *
 * <h3>易错点:</h3>
 * volatile i++ 不是原子操作！它涉及读取-修改-写入操作。
 * 使用 AtomicInteger 进行原子自增操作。
 *
 * <h3>核心理解:</h3>
 * Volatile 是 Java 中最轻量的同步机制。
 * 适用于简单标志或单写者场景。
 *
 * TODO: 移除 volatile 并观察会发生什么 - 循环会终止吗？
 */
public class VolatileDemo {

    private static volatile boolean flag = false;

    /**
     * 演示 volatile 的可见性保证。
     * 没有 volatile，主线程可能永远看不到标志的变化。
     */
    public void testVisibility() throws InterruptedException {
        new Thread(() -> {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            flag = true;
            System.out.println("子线程: 我已经把 flag 改为 true 了，大家都能看到吗？");
        }).start();

        // 没有 volatile 这个自旋等待可能永远循环
        while (!flag) {
            // 什么也不做 - 只是等待可见性
        }
        System.out.println("主线程: 我看到 flag 变了! 任务完成。");
    }

    /**
     * 演示 volatile 不保证原子性。
     *
     * TODO: 多次运行并观察最终计数通常小于预期值。
     */
    public void testNonAtomicity() throws InterruptedException {
        final int threadCount = 10;
        final int incrementsPerThread = 1000;

        VolatileCounter counter = new VolatileCounter();
        Thread[] threads = new Thread[threadCount];

        for (int i = 0; i < threadCount; i++) {
            threads[i] = new Thread(() -> {
                for (int j = 0; j < incrementsPerThread; j++) {
                    counter.increment();
                }
            });
            threads[i].start();
        }

        for (Thread thread : threads) {
            thread.join();
        }

        System.out.println("预期值: " + (threadCount * incrementsPerThread));
        System.out.println("实际值: " + counter.getCount());
        System.out.println("由于非原子自增导致的更新丢失!");
    }

    private static class VolatileCounter {
        private volatile int count = 0;

        public void increment() {
            count++; // 非原子操作! 读取-修改-写入
        }

        public int getCount() {
            return count;
        }
    }
}
