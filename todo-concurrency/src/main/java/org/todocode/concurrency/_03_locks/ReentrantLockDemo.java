package org.todocode.concurrency._03_locks;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * [TodoCode] ReentrantLock 演示
 *
 * <h3>背景:</h3>
 * ReentrantLock 是一个显式锁，比 synchronized 提供更多灵活性。
 * 它支持公平性、可中断的锁获取和尝试锁模式。
 *
 * <h3>易错点:</h3>
 * 始终使用 try-finally 确保锁释放！忘记 unlock
 * 会导致线程永久阻塞。
 *
 * <h3>核心理解:</h3>
 * ReentrantLock 是“可重入”的 - 同一线程可以多次获取它。
 * 每个 lock() 必须与一个 unlock() 配对。
 *
 * TODO: 公平锁和非公平锁在吞吐量方面有什么区别？
 */
public class ReentrantLockDemo {

    private final ReentrantLock fairLock = new ReentrantLock(true);   // 公平锁
    private final ReentrantLock unfairLock = new ReentrantLock(false); // 非公平锁(默认)

    private int counter = 0;

    /**
     * 演示使用 try-finally 模式的基本锁用法。
     * 始终使用这种模式确保锁释放。
     */
    public void safeIncrement() {
        unfairLock.lock();
        try {
            counter++;
        } finally {
            unfairLock.unlock(); // 关键: 始终在 finally 块中
        }
    }

    /**
     * 演示带超时的 tryLock - 避免无限期等待。
     *
     * @return 如果获取了锁并完成操作则返回 true
     */
    public boolean tryIncrementWithTimeout() {
        try {
            if (unfairLock.tryLock(1, TimeUnit.SECONDS)) {
                try {
                    counter++;
                    return true;
                } finally {
                    unfairLock.unlock();
                }
            }
            return false;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return false;
        }
    }

    /**
     * 演示可中断的锁获取。
     * 当你需要取消等待中的线程时很有用。
     */
    public void interruptibleLockDemo() throws InterruptedException {
        unfairLock.lockInterruptibly();
        try {
            // 临界区
            counter++;
        } finally {
            unfairLock.unlock();
        }
    }

    /**
     * 演示可重入性 - 同一线程可以多次获取锁。
     */
    public void reentrantDemo() {
        unfairLock.lock();
        try {
            System.out.println("第一次获取锁。持有计数: " + unfairLock.getHoldCount());
            unfairLock.lock(); // 可重入 - 同一线程再次获取
            try {
                System.out.println("第二次获取锁。持有计数: " + unfairLock.getHoldCount());
            } finally {
                unfairLock.unlock();
            }
            System.out.println("第二次释放锁。持有计数: " + unfairLock.getHoldCount());
        } finally {
            unfairLock.unlock();
        }
        System.out.println("第一次释放锁。持有计数: " + unfairLock.getHoldCount());
    }

    /**
     * 比较公平锁与非公平锁的性能。
     *
     * TODO: 运行此方法并观察吞吐量的差异。
     * 公平锁由于队列管理开销而吞吐量较低。
     */
    public void compareFairVsUnfair() throws InterruptedException {
        final int iterations = 100_000;

        // 测试非公平锁
        long start = System.nanoTime();
        for (int i = 0; i < iterations; i++) {
            unfairLock.lock();
            try {
                counter++;
            } finally {
                unfairLock.unlock();
            }
        }
        long unfairTime = System.nanoTime() - start;

        counter = 0;

        // 测试公平锁
        start = System.nanoTime();
        for (int i = 0; i < iterations; i++) {
            fairLock.lock();
            try {
                counter++;
            } finally {
                fairLock.unlock();
            }
        }
        long fairTime = System.nanoTime() - start;

        System.out.println("非公平锁时间: " + unfairTime / 1_000_000 + " ms");
        System.out.println("公平锁时间: " + fairTime / 1_000_000 + " ms");
        System.out.println("公平锁慢 " + (fairTime / unfairTime) + " 倍");
    }

    public int getCounter() {
        return counter;
    }
}
