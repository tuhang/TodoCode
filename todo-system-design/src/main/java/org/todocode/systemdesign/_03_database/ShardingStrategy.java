package org.todocode.systemdesign._03_database;

/**
 * [TodoCode] 数据库分片策略
 *
 * <h3>背景:</h3>
 * 分片将数据分布到多个数据库以处理规模。
 * 分片键决定每条数据存储在哪个分片。
 *
 * <h3>易错点:</h3>
 * 选择不好的分片键会导致热点。
 * 避免仅按时间戳或顺序 ID 分片。
 *
 * <h3>核心理解:</h3>
 * 好的分片键:
 * - 用户 ID: 均匀分布，用户数据局部性
 * - 租户 ID: 多租户的自然隔离
 * - 地理区域: 减少延迟
 */
public class ShardingStrategy {

    /**
     * 基于哈希的分片: 将数据均匀分布到各分片。
     */
    public static class HashSharding {
        private final int shardCount;

        public HashSharding(int shardCount) {
            this.shardCount = shardCount;
        }

        public int getShard(String key) {
            return Math.abs(key.hashCode() % shardCount);
        }

        public int getShard(long id) {
            return (int) (Math.abs(id) % shardCount);
        }
    }

    /**
     * 基于范围的分片: 适合范围查询。
     * TODO: 思考当范围变得不均匀时如何重新平衡。
     */
    public static class RangeSharding {
        private final long[] ranges; // [0-1000] -> 分片0, [1001-2000] -> 分片1

        public RangeSharding(long... boundaries) {
            this.ranges = boundaries;
        }

        public int getShard(long id) {
            for (int i = 0; i < ranges.length; i++) {
                if (id <= ranges[i]) {
                    return i;
                }
            }
            return ranges.length; // 溢出时使用最后一个分片
        }
    }

    /**
     * 一致性哈希: 分片变化时最小化重新映射。
     * 对于分布式缓存至关重要。
     */
    public static class ConsistentHashing {
        // TODO: 实现虚拟节点以获得更好的分布
        // 参考: https://en.wikipedia.org/wiki/Consistent_hashing
    }
}
