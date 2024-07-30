package com.sjxm.sjxmrpc.fault.tolerant;

/**
 * @Author: 四季夏目
 * @Date: 2024/7/30
 * @Description:
 */

import com.sjxm.sjxmrpc.model.RpcResponse;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * 静默处理异常 - 容错策略
 */
@Slf4j
public class FailSafeTolerantStrategy implements TolerantStrategy{


    @Override
    public RpcResponse doTolerant(Map<String, Object> context, Exception e) {
        log.info("静默处理异常",e);
        return new RpcResponse();
    }
}
