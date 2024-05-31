package com.sjxm.example.provider;

import com.sjxm.example.common.service.UserService;
import com.sjxm.sjxmrpc.RpcApplication;
import com.sjxm.sjxmrpc.registry.LocalRegistry;
import com.sjxm.sjxmrpc.server.HttpServer;
import com.sjxm.sjxmrpc.server.VertxHttpServer;

/**
 * 服务提供者示例
 */
public class ProviderExample {

    public static void main(String[] args) {
        //RPC框架初始化
        RpcApplication.init();

        //注册服务
        LocalRegistry.register(UserService.class.getName(),UserServiceImpl.class);

        //启动web服务
        HttpServer httpServer = new VertxHttpServer();
        httpServer.doStart(RpcApplication.getRpcConfig().getServerPort());
    }

}
