package com.sjxm.example.provider;

import com.sjxm.example.common.service.UserService;
import com.sjxm.sjxmrpc.RpcApplication;
import com.sjxm.sjxmrpc.config.RegistryConfig;
import com.sjxm.sjxmrpc.config.RpcConfig;
import com.sjxm.sjxmrpc.model.ServiceMetaInfo;
import com.sjxm.sjxmrpc.registry.LocalRegistry;
import com.sjxm.sjxmrpc.registry.Registry;
import com.sjxm.sjxmrpc.registry.RegistryFactory;
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
        String serviceName = UserService.class.getName();
        LocalRegistry.register(serviceName,UserServiceImpl.class);

        //注册服务到注册中心
        RpcConfig rpcConfig = RpcApplication.getRpcConfig();
        RegistryConfig rpcRegistryConfig = rpcConfig.getRegistryConfig();
        Registry registry = RegistryFactory.getInstance(rpcRegistryConfig.getRegistry());
        ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
        serviceMetaInfo.setServiceName(serviceName);
        serviceMetaInfo.setServiceAddress(rpcConfig.getServerHost() + ":" + rpcConfig.getServerPort());
        try{
            registry.register(serviceMetaInfo);
        }catch(Exception e){
            throw new RuntimeException(e);
        }

        //启动web服务
        HttpServer httpServer = new VertxHttpServer();
        httpServer.doStart(RpcApplication.getRpcConfig().getServerPort());
    }

}
