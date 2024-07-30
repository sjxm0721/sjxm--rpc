package com.sjxm.sjxmrpc.fault.retry;

/**
 * @Author: 四季夏目
 * @Date: 2024/7/30
 * @Description:
 */

/**
 * 重试策略键名常量
 */
public interface RetryStrategyKeys {

    /**
     * 不重试
     */
    String NO = "no";

    /**
     * 固定时间间隔
     */
    String FIXED_INTERVAL = "fixedInterval";
}
