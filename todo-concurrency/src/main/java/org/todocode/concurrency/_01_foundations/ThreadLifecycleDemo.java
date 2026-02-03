package org.todocode.concurrency._01_foundations;

import java.util.concurrent.TimeUnit;

/**
 * [TodoCode] 线程生命周期演示
 *
 * <h3>背景:</h3>
 * 本类演示线程可能处于的不同状态，
 * 以及状态之间如何转换。
 *
 * <h3>易错点:</h3>
 * Thread.sleep() 不会释放锁，而 Object.wait() 会释放锁。
 * 这是死锁的常见来源。
 *
 * <h3>核心理解:</h3>
 * 把线程状态想象成一个状态机 - 理解状态转换
 * 有助于预测和控制线程行为。
 *
 * TODO: 通过添加断点并检查 Thread.getState() 来观察每个线程状态
 */
public class ThreadLifecycleDemo {

    /**
     * 演示线程状态: NEW -> RUNNABLE -> TERMINATED
     */
    public void demonstrateBasicLifecycle() {
        Thread thread = new Thread(() -> {
            System.out.println("Thread is running: " + Thread.currentThread().getState());
        });

        System.out.println("创建后: " + thread.getState()); // NEW
        thread.start();
        System.out.println("启动后: " + thread.getState()); // RUNNABLE

        try {
            thread.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("完成后: " + thread.getState()); // TERMINATED
    }

    /**
     * 使用 Thread.sleep() 演示 TIMED_WAITING 状态
     */
    public void demonstrateTimedWaiting() throws InterruptedException {
        Thread sleepingThread = new Thread(() -> {
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        sleepingThread.start();
        TimeUnit.MILLISECONDS.sleep(100); // 给线程时间进入睡眠状态

        System.out.println("睡眠线程状态: " + sleepingThread.getState()); // TIMED_WAITING

        sleepingThread.interrupt();
        sleepingThread.join();
    }

    /**
     * 演示线程中断机制
     *
     * TODO: 如果对一个不在阻塞状态的线程调用 interrupt() 会发生什么？
     */
    public void demonstrateInterruption() {
        Thread interruptibleThread = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                // 模拟工作
                System.out.println("工作中...");
                try {
                    TimeUnit.MILLISECONDS.sleep(500);
                } catch (InterruptedException e) {
                    System.out.println("睡眠期间被中断!");
                    Thread.currentThread().interrupt(); // 恢复中断状态
                    break;
                }
            }
            System.out.println("线程优雅结束");
        });

        interruptibleThread.start();

        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        interruptibleThread.interrupt();
    }
}
