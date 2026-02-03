package org.todocode.concurrency._07_patterns;

import java.util.concurrent.*;

/**
 * [TodoCode] 生产者-消费者模式演示
 *
 * <h3>背景:</h3>
 * 生产者-消费者模式通过缓冲区(通常是 BlockingQueue)
 * 将任务生产与消费解耦。这是异步处理系统的基础。
 *
 * <h3>易错点:</h3>
 * 使用无界队列可能导致负载下的 OutOfMemoryError。
 * 始终使用有界队列并正确处理背压。
 *
 * <h3>核心理解:</h3>
 * BlockingQueue 方法:
 * - put()/take(): 无限期阻塞
 * - offer()/poll(): 立即返回 boolean/null
 * - offer(timeout)/poll(timeout): 带超时阻塞
 *
 * 根据你对阻塞的容忍度选择。
 */
public class ProducerConsumerDemo {

    private final BlockingQueue<String> queue;
    private final int poisonPillCount;
    private volatile boolean running = true;

    public ProducerConsumerDemo(int capacity, int consumerCount) {
        this.queue = new ArrayBlockingQueue<>(capacity);
        this.poisonPillCount = consumerCount;
    }

    /**
     * 生成任务的生产者。
     */
    public void produce(String producerName, int taskCount) {
        new Thread(() -> {
            try {
                for (int i = 0; i < taskCount && running; i++) {
                    String task = producerName + "-Task-" + i;
                    queue.put(task); // 队列满时阻塞
                    System.out.println("[" + producerName + "] 生产: " + task);
                    TimeUnit.MILLISECONDS.sleep(100);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }, producerName).start();
    }

    /**
     * 处理任务的消费者。
     */
    public void consume(String consumerName) {
        new Thread(() -> {
            try {
                while (true) {
                    String task = queue.take(); // 队列空时阻塞
                    if ("POISON_PILL".equals(task)) {
                        System.out.println("[" + consumerName + "] 收到毒丸，正在关闭。");
                        break;
                    }
                    System.out.println("[" + consumerName + "] 消费: " + task);
                    TimeUnit.MILLISECONDS.sleep(200); // 模拟处理
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }, consumerName).start();
    }

    /**
     * 使用毒丸进行优雅关闭。
     */
    public void shutdown() {
        running = false;
        try {
            for (int i = 0; i < poisonPillCount; i++) {
                queue.put("POISON_PILL");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * 展示模式实际运行的演示运行器。
     */
    public static void runDemo() throws InterruptedException {
        int consumerCount = 2;
        ProducerConsumerDemo demo = new ProducerConsumerDemo(5, consumerCount);

        // 启动消费者
        for (int i = 0; i < consumerCount; i++) {
            demo.consume("Consumer-" + i);
        }

        // 启动生产者
        demo.produce("Producer", 10);

        // 让它运行一段时间
        TimeUnit.SECONDS.sleep(5);

        // 优雅关闭
        demo.shutdown();
    }
}
