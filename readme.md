# TodoCode

> 一个以知识驱动的 Java 后端代码片段仓库，专注于 JUC 并发编程、系统设计、AI 集成与算法训练。

## 项目简介

TodoCode 是一个多模块的学习型代码仓库，采用 **"Maven 多模块 + 单元测试驱动学习"** 的结构设计。代码既是**可验证的测试用例**，又是**互动式的学习路径**。

### 核心特点

- **模块化设计**: 按知识领域拆分，结构清晰
- **测试驱动**: 用 JUnit 验证理论，而非 main 方法
- **自包含**: 每个代码片段都有完整的 Javadoc 说明
- **TODO 思考题**: 引导式学习，边做边思考

## 技术栈

| 技术 | 版本 | 说明 |
|------|------|------|
| Java | 25 | 启用预览特性 |
| Spring Boot | 3.4.2 | 最新稳定版 |
| Maven | 3.9+ | 多模块构建 |
| JUnit 5 | - | 测试框架 |
| Testcontainers | 1.19.7 | 集成测试 |
| Docker | - | 本地环境 |

## 项目结构

```
TodoCode/
├── pom.xml                          # 父工程 POM
├── docker-compose.yml               # 本地开发环境
├── .env.example                     # 环境变量模板
│
├── todo-concurrency/                # 【核心】JUC 并发编程模块
│   └── src/main/java/org/todocode/concurrency/
│       ├── _01_foundations/         # 基础概念 (线程生命周期、中断)
│       ├── _02_safety/              # 线程安全 (原子性、可见性、有序性)
│       ├── _03_locks/               # 锁机制 (Synchronized vs ReentrantLock)
│       ├── _04_tools/               # 并发工具 (CountDownLatch, Semaphore)
│       ├── _05_collections/         # 并发容器 (ConcurrentHashMap, CopyOnWrite)
│       ├── _06_executors/           # 线程池与异步 (Future, CompletableFuture)
│       └── _07_patterns/            # 并发设计模式 (生产者-消费者)
│
├── todo-system-design/              # 系统设计/架构模式模块
│   └── src/main/java/org/todocode/systemdesign/
│       ├── _01_patterns/            # 设计模式 (限流器)
│       ├── _02_caching/             # 缓存策略 (LRU)
│       └── _03_database/            # 数据库设计 (分片策略)
│
├── todo-ai-integrations/            # AI 集成与应用模块
│   └── src/main/java/org/todocode/ai/
│       └── _01_prompts/             # 提示词工程
│
├── todo-algorithms/                 # 算法与思维训练模块
│   └── src/main/java/org/todocode/algorithms/
│       └── _01_sorting/             # 排序算法 (快排、归并、堆排)
│
└── docker/                          # Docker 配置文件
    ├── mysql/init/                  # MySQL 初始化脚本
    └── prometheus/                  # Prometheus 配置
```

## 快速开始

### 1. 环境要求

- JDK 25+
- Maven 3.9+
- Docker & Docker Compose

### 2. 克隆项目

```bash
git clone https://github.com/your-username/TodoCode.git
cd TodoCode
```

### 3. 启动本地环境

```bash
# 启动 Docker 服务 (MySQL, Redis, RabbitMQ 等)
docker-compose up -d

# 查看服务状态
docker-compose ps
```

### 4. 构建项目

```bash
# 编译所有模块
mvn clean install

# 跳过测试编译
mvn clean install -DskipTests
```

### 5. 运行测试

```bash
# 运行所有测试
mvn test

# 运行指定模块测试
mvn test -pl todo-concurrency

# 运行指定测试类
mvn test -pl todo-algorithms -Dtest=SortingAlgorithmsTest
```

## Docker 服务

| 服务 | 端口 | 用途 | 访问地址 |
|------|------|------|----------|
| MySQL | 3306 | 数据库 | `localhost:3306` |
| Redis | 6379 | 缓存 | `localhost:6379` |
| Redis Commander | 8081 | Redis GUI | http://localhost:8081 |
| RabbitMQ | 5672/15672 | 消息队列 | http://localhost:15672 |
| MinIO | 9000/9001 | 对象存储 | http://localhost:9001 |
| Prometheus | 9090 | 指标监控 | http://localhost:9090 |
| Grafana | 3000 | 可视化 | http://localhost:3000 |

**默认凭证:**
- MySQL: `todocode / todocode123`
- RabbitMQ: `todocode / todocode123`
- MinIO: `minioadmin / minioadmin123`
- Grafana: `admin / admin123`

## 模块详解

### todo-concurrency (JUC 并发编程)

这是本仓库的**核心模块**，按并发特性/场景分类，而非按类名分类。

| 包 | 内容 | 关键类 |
|----|------|--------|
| `_01_foundations` | 线程基础 | `ThreadLifecycleDemo` |
| `_02_safety` | 线程安全 | `VolatileDemo` |
| `_03_locks` | 锁机制 | `ReentrantLockDemo` |
| `_04_tools` | 并发工具 | `ConcurrencyToolsDemo` |
| `_05_collections` | 并发容器 | `ConcurrentCollectionsDemo` |
| `_06_executors` | 线程池 | `ThreadPoolDemo` |
| `_07_patterns` | 并发模式 | `ProducerConsumerDemo` |

### todo-system-design (系统设计)

涵盖后端系统设计的常见模式和算法。

| 包 | 内容 | 关键类 |
|----|------|--------|
| `_01_patterns` | 设计模式 | `TokenBucketRateLimiter` |
| `_02_caching` | 缓存策略 | `LRUCache` |
| `_03_database` | 数据库设计 | `ShardingStrategy` |

### todo-ai-integrations (AI 集成)

AI/LLM 相关的集成模式和最佳实践。

| 包 | 内容 | 关键类 |
|----|------|--------|
| `_01_prompts` | 提示词工程 | `PromptTemplates` |

### todo-algorithms (算法)

经典算法实现与分析。

| 包 | 内容 | 关键类 |
|----|------|--------|
| `_01_sorting` | 排序算法 | `SortingAlgorithms` |

## 代码风格

### Javadoc 规范

每个类都应包含结构化的 Javadoc：

```java
/**
 * [TodoCode] 类名/功能描述
 *
 * <h3>背景:</h3>
 * 为什么要写这个片段？
 *
 * <h3>易错点:</h3>
 * 这里容易踩什么坑？
 *
 * <h3>核心理解:</h3>
 * 你的独家思考。
 *
 * TODO: 思考题
 */
```

### 测试规范

- 使用 JUnit 5 + `@DisplayName` 注解
- 测试方法名使用中文描述
- 每个演示类都应有对应的测试类

## 贡献指南

1. Fork 本仓库
2. 创建特性分支 (`git checkout -b feature/amazing-feature`)
3. 提交更改 (`git commit -m '添加某个特性'`)
4. 推送到分支 (`git push origin feature/amazing-feature`)
5. 创建 Pull Request

## 许可证

本项目采用 MIT 许可证 - 详见 [LICENSE](LICENSE) 文件。

---

> **TodoCode** - 把技术难点变成待办清单，一个个攻克！
