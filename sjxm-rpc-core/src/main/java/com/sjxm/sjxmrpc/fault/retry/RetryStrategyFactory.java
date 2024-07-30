package com.sjxm.sjxmrpc.fault.retry;

/**
 * @Author: 四季夏目
 * @Date: 2024/7/30
 * @Description:
 */

import com.sjxm.sjxmrpc.spi.SpiLoader;

/**
 * 重试策略工厂(用于获取重试器对象)
 */
public class RetryStrategyFactory {

    static{
        SpiLoader.load(RetryStrategy.class);
    }

    /**
     * 默认重试器
     */
    private static final RetryStrategy DEFAULT_RETRY_STRATEGY = new NoRetryStrategy();

    /**
     * 获取实例
     * @param key
     * @return
     */
    public static RetryStrategy getInstance(String key){
        return SpiLoader.getInstance(RetryStrategy.class,key);
    }

}
