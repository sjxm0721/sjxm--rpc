package com.sjxm.sjxmrpc.loadbalancer;

import com.sjxm.sjxmrpc.spi.SpiLoader;

/**
 * @Author: 四季夏目
 * @Date: 2024/7/24
 * @Description:
 */
public class LoadBalancerFactory {
    static {
        SpiLoader.load(LoadBalancer.class);
    }

    /**
     * 默认负载均衡器
     */
    private static final LoadBalancer DEFAULT_LOAD_BALANCER = new RoundRobinLoadBalancer();

    /**
     * 获取实例
     * @param key
     * @return
     */
    public static LoadBalancer getInstance(String key){
        return SpiLoader.getInstance(LoadBalancer.class,key);
    }
}
