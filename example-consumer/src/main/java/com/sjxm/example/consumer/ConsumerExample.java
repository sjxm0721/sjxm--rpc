package com.sjxm.example.consumer;

import com.sjxm.sjxmrpc.config.RpcConfig;
import com.sjxm.sjxmrpc.utils.ConfigUtils;

/**
 * 服务消费者示例
 */
public class ConsumerExample {

    public static void main(String[] args) {
        RpcConfig rpc = ConfigUtils.loadConfig(RpcConfig.class,"rpc");
        System.out.println(rpc);
    }

}
