package com.sjxm.sjxmrpc.loadbalancer;

/**
 * @Author: 四季夏目
 * @Date: 2024/7/24
 * @Description:
 */

/**
 * 负载均衡器键名常量
 */
public interface LoadBalancerKeys {

    /**
     * 轮询
     */
    String ROUND_ROBIN = "roundRobin";

    /**
     * 随机
     */
    String RANDOM = "random";

    /**
     * 一致性Hash
     */
    String CONSISTENT_HASH = "consistentHash";

}
