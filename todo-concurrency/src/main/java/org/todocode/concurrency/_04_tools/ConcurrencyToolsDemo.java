package org.todocode.concurrency._04_tools;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * [TodoCode] 并发工具演示
 *
 * <h3>背景:</h3>
 * JUC 提供了多种用于线程协调的同步工具:
 * - CountDownLatch: 一次性倒计时屏障
 * - CyclicBarrier: 可重用的线程互等屏障
 * - Semaphore: 控制对有限资源的访问
 *
 * <h3>易错点:</h3>
 * CountDownLatch 无法重置 - 如果需要重用请使用 CyclicBarrier。
 * 不要忘记正确处理 InterruptedException。
 *
 * <h3>核心理解:</h3>
 * 选择正确的工具:
 * - CountDownLatch: "等待 N 个事件完成"
 * - CyclicBarrier: "等待 N 个线程到达同一点"
 * - Semaphore: "限制并发访问数为 N"
 */
public class ConcurrencyToolsDemo {

    /**
     * CountDownLatch 演示: 主线程等待所有工作线程完成。
     *
     * TODO: 如果某个工作线程抛出异常并且永远不调用 countDown() 会发生什么？
     */
    public void countDownLatchDemo() throws InterruptedException {
        int workerCount = 5;
        CountDownLatch latch = new CountDownLatch(workerCount);

        System.out.println("老板: 启动 " + workerCount + " 个工人...");

        for (int i = 0; i < workerCount; i++) {
            final int workerId = i;
            new Thread(() -> {
                try {
                    TimeUnit.MILLISECONDS.sleep((long) (Math.random() * 1000));
                    System.out.println("工人 " + workerId + " 完成!");
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    latch.countDown(); // 始终要计数，即使失败也要
                }
            }).start();
        }

        latch.await(); // 等待所有工人
        System.out.println("老板: 所有工人都完成了! 下班回家。");
    }

    /**
     * CyclicBarrier 演示: 线程在屏障点互相等待。
     *
     * TODO: CyclicBarrier 和 CountDownLatch 有什么区别？
     */
    public void cyclicBarrierDemo() throws Exception {
        int partyCount = 3;
        CyclicBarrier barrier = new CyclicBarrier(partyCount, () -> {
            System.out.println("=== 所有线程到达屏障! ===");
        });

        for (int i = 0; i < partyCount; i++) {
            final int threadId = i;
            new Thread(() -> {
                try {
                    System.out.println("线程 " + threadId + " 开始第一阶段...");
                    TimeUnit.MILLISECONDS.sleep((long) (Math.random() * 500));
                    System.out.println("线程 " + threadId + " 在屏障处等待 (第一阶段)");
                    barrier.await();

                    System.out.println("线程 " + threadId + " 开始第二阶段...");
                    TimeUnit.MILLISECONDS.sleep((long) (Math.random() * 500));
                    System.out.println("线程 " + threadId + " 在屏障处等待 (第二阶段)");
                    barrier.await();

                    System.out.println("线程 " + threadId + " 完成所有阶段!");
                } catch (Exception e) {
                    Thread.currentThread().interrupt();
                }
            }).start();
        }

        TimeUnit.SECONDS.sleep(3); // 等待演示完成
    }

    /**
     * Semaphore 演示: 限制对资源的并发访问。
     *
     * TODO: 想想这如何用于限流。
     */
    public void semaphoreDemo() throws InterruptedException {
        int permits = 2; // 同时只有 2 个线程可以访问
        Semaphore semaphore = new Semaphore(permits);

        int threadCount = 5;
        for (int i = 0; i < threadCount; i++) {
            final int threadId = i;
            new Thread(() -> {
                try {
                    System.out.println("线程 " + threadId + " 请求许可...");
                    semaphore.acquire();
                    System.out.println("线程 " + threadId + " 获得许可! (可用: " + semaphore.availablePermits() + ")");

                    TimeUnit.SECONDS.sleep(1); // 模拟工作

                    System.out.println("线程 " + threadId + " 释放许可。");
                    semaphore.release();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }).start();
        }

        TimeUnit.SECONDS.sleep(5); // 等待演示完成
    }
}
