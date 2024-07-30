package com.sjxm.sjxmrpc.fault.tolerant;

import com.sjxm.sjxmrpc.model.RpcResponse;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * @Author: 四季夏目
 * @Date: 2024/7/30
 * @Description:
 */
@Slf4j
public class FailOverTolerantStrategy implements TolerantStrategy{
    @Override
    public RpcResponse doTolerant(Map<String, Object> context, Exception e) {
        //todo 获取其他服务节点并调用
        return null;
    }
}
