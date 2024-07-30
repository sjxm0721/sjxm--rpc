package com.sjxm.sjxmrpc.fault.retry;

/**
 * @Author: 四季夏目
 * @Date: 2024/7/30
 * @Description:
 */

import com.sjxm.sjxmrpc.model.RpcResponse;

import java.util.concurrent.Callable;

/**
 * 重试策略
 */
public interface RetryStrategy {

    /**
     * 重试
     * @param callable
     * @return
     * @throws Exception
     */
    RpcResponse doRetry(Callable<RpcResponse> callable) throws Exception;

}
