package com.sjxm.sjxmrpc.fault.retry;

import com.sjxm.sjxmrpc.model.RpcResponse;

import java.util.concurrent.Callable;

/**
 * @Author: 四季夏目
 * @Date: 2024/7/30
 * @Description:
 */

/**
 * 重试策略-不重试
 */
public class NoRetryStrategy implements RetryStrategy{
    /**
     * 重试
     * @param callable
     * @return
     * @throws Exception
     */
    @Override
    public RpcResponse doRetry(Callable<RpcResponse> callable) throws Exception {
        return callable.call();
    }
}
