/**
 * [TodoCode] 基础篇 - 线程生命周期
 *
 * <h3>背景:</h3>
 * 理解线程状态是学习JUC的基础。线程会经历以下状态转换：
 * NEW -> RUNNABLE -> (BLOCKED/WAITING/TIMED_WAITING) -> TERMINATED
 *
 * <h3>易错点:</h3>
 * 不要混淆 BLOCKED 和 WAITING - 它们由不同的机制触发：
 * BLOCKED: 等待监视器锁 (synchronized)
 * WAITING: 等待信号 (wait/join/park)
 *
 * <h3>核心理解:</h3>
 * 线程状态转换是并发程序的心跳。
 * 掌握这个，调试并发问题就会容易得多。
 */
package org.todocode.concurrency._01_foundations;
