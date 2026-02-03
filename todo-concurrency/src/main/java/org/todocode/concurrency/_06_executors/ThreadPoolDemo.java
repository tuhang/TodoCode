package org.todocode.concurrency._06_executors;

import java.util.List;
import java.util.concurrent.*;

/**
 * [TodoCode] 线程池与 Executors 演示
 *
 * <h3>背景:</h3>
 * 线程池管理线程生命周期并提供任务调度。
 * 始终优先使用 ExecutorService 而不是创建原始线程。
 *
 * <h3>易错点:</h3>
 * 永远不要在生产环境使用 Executors.newCachedThreadPool() - 它可能创建
 * 无限多的线程。始终使用显式边界的 ThreadPoolExecutor。
 *
 * <h3>核心理解:</h3>
 * ThreadPoolExecutor 的 7 个参数至关重要:
 * corePoolSize, maxPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler
 *
 * TODO 思考题:
 * 如果 corePoolSize 满了，队列也满了，
 * 线程池会优先创建非核心线程还是直接拒绝？
 * -> 运行 testRejectPolicy() 来验证你的猜想。
 */
public class ThreadPoolDemo {

    /**
     * 创建正确配置的 ThreadPoolExecutor。
     * 这是创建线程池的推荐方式。
     */
    public ThreadPoolExecutor createProperThreadPool() {
        return new ThreadPoolExecutor(
                2,                      // 核心线程数
                5,                      // 最大线程数
                60, TimeUnit.SECONDS,   // 空闲线程存活时间
                new ArrayBlockingQueue<>(10),  // 有界队列!
                new ThreadFactory() {
                    private int counter = 0;
                    @Override
                    public Thread newThread(Runnable r) {
                        Thread t = new Thread(r);
                        t.setName("todocode-worker-" + counter++);
                        t.setDaemon(false);
                        return t;
                    }
                },
                new ThreadPoolExecutor.CallerRunsPolicy() // 背压
        );
    }

    /**
     * 演示任务提交和结果获取。
     */
    public void submitTasksDemo() throws Exception {
        ExecutorService executor = createProperThreadPool();

        try {
            // 提交 Callable - 返回结果
            Future<String> future = executor.submit(() -> {
                TimeUnit.SECONDS.sleep(1);
                return "任务由 " + Thread.currentThread().getName() + " 完成";
            });

            System.out.println("任务已提交，正在做其他工作...");
            String result = future.get(5, TimeUnit.SECONDS); // 带超时的阻塞等待
            System.out.println("结果: " + result);

            // 提交 Runnable - 无返回值
            executor.execute(() -> {
                System.out.println("即发即忘任务由 " + Thread.currentThread().getName() + " 执行");
            });

        } finally {
            shutdownGracefully(executor);
        }
    }

    /**
     * 演示线程池过载时的拒绝策略。
     *
     * TODO: 尝试不同的拒绝策略并观察行为。
     */
    public void testRejectPolicy() {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                1, 1, 0, TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(1), // 非常小的队列
                new ThreadPoolExecutor.AbortPolicy() // 抛出异常
        );

        try {
            // 提交会溢出的任务
            for (int i = 0; i < 5; i++) {
                final int taskId = i;
                try {
                    executor.execute(() -> {
                        try {
                            System.out.println("任务 " + taskId + " 运行中...");
                            TimeUnit.SECONDS.sleep(2);
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                    });
                    System.out.println("任务 " + taskId + " 提交成功");
                } catch (RejectedExecutionException e) {
                    System.out.println("任务 " + taskId + " 被拒绝!");
                }
            }
        } finally {
            shutdownGracefully(executor);
        }
    }

    /**
     * 演示用于异步编程的 CompletableFuture。
     * 这是 Java 中处理异步操作的现代方式。
     */
    public void completableFutureDemo() throws Exception {
        ExecutorService executor = Executors.newFixedThreadPool(3);

        try {
            CompletableFuture<String> future = CompletableFuture
                    .supplyAsync(() -> {
                        System.out.println("步骤 1: 获取数据...");
                        return "rawData";
                    }, executor)
                    .thenApplyAsync(data -> {
                        System.out.println("步骤 2: 处理中...");
                        return data.toUpperCase();
                    }, executor)
                    .thenApplyAsync(data -> {
                        System.out.println("步骤 3: 丰富化...");
                        return data + "_ENRICHED";
                    }, executor);

            System.out.println("最终结果: " + future.get());

            // 组合多个 future
            CompletableFuture<String> future1 = CompletableFuture.supplyAsync(() -> "Hello", executor);
            CompletableFuture<String> future2 = CompletableFuture.supplyAsync(() -> "World", executor);

            String combined = future1.thenCombine(future2, (s1, s2) -> s1 + " " + s2).get();
            System.out.println("组合结果: " + combined);

        } finally {
            shutdownGracefully(executor);
        }
    }

    /**
     * 正确关闭 ExecutorService 的方式。
     * 始终使用这种模式!
     */
    private void shutdownGracefully(ExecutorService executor) {
        executor.shutdown();
        try {
            if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
                List<Runnable> dropped = executor.shutdownNow();
                System.out.println("丢弃了 " + dropped.size() + " 个任务");
                if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
                    System.err.println("执行器未能终止!");
                }
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}
