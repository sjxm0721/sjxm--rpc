package com.sjxm.sjxmrpc.fault.tolerant;

import com.sjxm.sjxmrpc.model.RpcResponse;

import java.util.Map;

/**
 * @Author: 四季夏目
 * @Date: 2024/7/30
 * @Description:
 */

/**
 * 快速失败 - 容错策略(立刻通知外层调用方)
 */
public class FailFastTolerantStrategy implements TolerantStrategy{
    @Override
    public RpcResponse doTolerant(Map<String, Object> context, Exception e) {
        throw new RuntimeException("服务报错",e);
    }
}
