package org.todocode.concurrency._03_locks;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

/**
 * [TodoCode] ReentrantLock 测试
 *
 * 这些测试验证 ReentrantLock 的行为并演示
 * 如何正确测试并发代码。
 */
class ReentrantLockTest {

    @Test
    @DisplayName("多线程应该安全地增加计数器")
    void testConcurrentIncrement() throws InterruptedException {
        ReentrantLockDemo demo = new ReentrantLockDemo();
        int threadCount = 10;
        int incrementsPerThread = 1000;
        CountDownLatch latch = new CountDownLatch(threadCount);

        ExecutorService executor = Executors.newFixedThreadPool(threadCount);

        for (int i = 0; i < threadCount; i++) {
            executor.submit(() -> {
                try {
                    for (int j = 0; j < incrementsPerThread; j++) {
                        demo.safeIncrement();
                    }
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await(10, TimeUnit.SECONDS);
        executor.shutdown();

        assertEquals(threadCount * incrementsPerThread, demo.getCounter(),
                "Counter should be exactly " + (threadCount * incrementsPerThread));
    }

    @Test
    @DisplayName("锁可用时 tryLock 带超时应该成功")
    void testTryLockSuccess() {
        ReentrantLockDemo demo = new ReentrantLockDemo();
        assertTrue(demo.tryIncrementWithTimeout(), "应该成功获取锁");
        assertEquals(1, demo.getCounter());
    }

    @Test
    @DisplayName("可重入锁应该允许同一线程多次获取")
    void testReentrancy() {
        ReentrantLockDemo demo = new ReentrantLockDemo();
        // 这不应该死锁
        assertDoesNotThrow(demo::reentrantDemo);
    }
}
