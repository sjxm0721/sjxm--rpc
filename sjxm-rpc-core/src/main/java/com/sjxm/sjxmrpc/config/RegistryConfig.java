package com.sjxm.sjxmrpc.config;

import lombok.Data;

/**
 * RPC框架注册中心配置
 */
@Data
public class RegistryConfig {

    /**
     * 注册中心类别
     */
    private String registry = "zookeeper";

    /**
     * 注册中心地址
     */
    private String address = "http://localhost:2181";

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 超时时间(单位毫秒)
     */
    private Long timeout=10000L;
}
