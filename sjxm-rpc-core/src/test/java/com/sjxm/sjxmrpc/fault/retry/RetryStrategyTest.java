package com.sjxm.sjxmrpc.fault.retry;

import com.sjxm.sjxmrpc.model.RpcResponse;
import junit.framework.TestCase;

/**
 * @Author: 四季夏目
 * @Date: 2024/7/30
 * @Description:
 */

/**
 * 重试策略测试
 */
public class RetryStrategyTest extends TestCase {

    RetryStrategy retryStrategy = new NoRetryStrategy();
    public void testDoRetry() {

        try{
            RpcResponse rpcResponse = retryStrategy.doRetry(()->{
                System.out.println("测试重试");
                throw new RuntimeException("模拟重试失败");
            });
            System.out.println(rpcResponse);
        }catch (Exception e){
            System.out.println("重试多次失败");
            e.printStackTrace();
        }

    }
}