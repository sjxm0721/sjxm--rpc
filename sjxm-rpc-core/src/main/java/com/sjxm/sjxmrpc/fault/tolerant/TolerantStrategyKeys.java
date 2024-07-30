package com.sjxm.sjxmrpc.fault.tolerant;

/**
 * @Author: 四季夏目
 * @Date: 2024/7/30
 * @Description:
 */

/**
 * 容错策略键名常量
 */
public interface TolerantStrategyKeys {

    /**
     * 故障恢复
     */
    String FAIL_BACK = "failBack";

    /**
     * 快速失败
     */
    String FAIL_FAST = "failFast";

    /**
     * 故障转移
     */
    String FAIL_OVER = "failOver";

    /**
     * 静默处理
     */
    String FAIL_SAFE = "failSafe";
}
