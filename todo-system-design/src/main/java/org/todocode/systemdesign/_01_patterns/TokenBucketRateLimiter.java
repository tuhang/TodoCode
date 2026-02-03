package org.todocode.systemdesign._01_patterns;

/**
 * [TodoCode] 限流器 - 令牌桶算法
 *
 * <h3>背景:</h3>
 * 限流对于保护 API 免受滥用至关重要。
 * 令牌桶允许突发流量同时保持平均速率。
 *
 * <h3>易错点:</h3>
 * 不要使用 System.currentTimeMillis() 进行高精度计时。
 * 使用 System.nanoTime() 进行时间间隔计算。
 *
 * <h3>核心理解:</h3>
 * 令牌桶 vs 漏桶:
 * - 令牌桶: 允许突发，对突发流量更平滑
 * - 漏桶: 固定速率输出，适合平滑处理
 */
public class TokenBucketRateLimiter {

    private final long maxTokens;
    private final long refillRate; // 每秒令牌数
    private long currentTokens;
    private long lastRefillTime;

    public TokenBucketRateLimiter(long maxTokens, long refillRate) {
        this.maxTokens = maxTokens;
        this.refillRate = refillRate;
        this.currentTokens = maxTokens;
        this.lastRefillTime = System.nanoTime();
    }

    /**
     * 尝试获取一个许可。
     *
     * @return 如果获取许可则返回 true，如果被限流则返回 false
     */
    public synchronized boolean tryAcquire() {
        refill();
        if (currentTokens > 0) {
            currentTokens--;
            return true;
        }
        return false;
    }

    /**
     * 获取许可，必要时阻塞。
     * TODO: 使用正确的 wait/notify 机制实现
     */
    public synchronized void acquire() throws InterruptedException {
        while (!tryAcquire()) {
            long waitTime = calculateWaitTime();
            if (waitTime > 0) {
                wait(waitTime);
            }
        }
    }

    private void refill() {
        long now = System.nanoTime();
        long elapsed = now - lastRefillTime;
        long tokensToAdd = elapsed * refillRate / 1_000_000_000L;

        if (tokensToAdd > 0) {
            currentTokens = Math.min(maxTokens, currentTokens + tokensToAdd);
            lastRefillTime = now;
        }
    }

    private long calculateWaitTime() {
        if (currentTokens > 0) return 0;
        return 1000 / refillRate; // 下一个令牌的毫秒数
    }

    public long getAvailableTokens() {
        return currentTokens;
    }
}
