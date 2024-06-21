package com.sjxm.sjxmrpc.config;

import com.sjxm.sjxmrpc.serializer.SerializerKeys;
import lombok.Data;

/**
 * RPC框架配置
 */
@Data
public class RpcConfig {

    /**
     * 名称
     */
    private String name = "sjxm-rpc";

    /**
     * 版本号
     */
    private String version = "1.0";

    /**
     * 服务器主机名
     */
    private String serverHost = "localhost";

    /**
     * 服务器端口号
     */
    private Integer serverPort = 8080;

    /**
     * 模拟调用
     */
    private boolean mock = false;


    private RegistryConfig registryConfig;

    /**
     * 序列化器
     */
    private String serializer = SerializerKeys.JDK;
}
