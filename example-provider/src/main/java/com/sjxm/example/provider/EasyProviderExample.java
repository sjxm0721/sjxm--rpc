package com.sjxm.example.provider;


import com.sjxm.example.common.service.UserService;
import com.sjxm.sjxmrpc.registry.LocalRegistry;
import com.sjxm.sjxmrpc.server.HttpServer;
import com.sjxm.sjxmrpc.server.VertxHttpServer;

/**
 * 简易服务提供者示例
 */
public class EasyProviderExample {
    public static void main(String[] args) {
        //注册服务
        LocalRegistry.register(UserService.class.getName(), UserServiceImpl.class);

        //启动web服务
        HttpServer httpServer = new VertxHttpServer();
        httpServer.doStart(8080);
    }

}
