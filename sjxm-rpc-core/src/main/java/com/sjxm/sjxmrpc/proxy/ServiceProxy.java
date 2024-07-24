package com.sjxm.sjxmrpc.proxy;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.sjxm.sjxmrpc.RpcApplication;
import com.sjxm.sjxmrpc.config.RpcConfig;
import com.sjxm.sjxmrpc.constant.RpcConstant;
import com.sjxm.sjxmrpc.loadbalancer.LoadBalancer;
import com.sjxm.sjxmrpc.loadbalancer.LoadBalancerFactory;
import com.sjxm.sjxmrpc.model.RpcRequest;
import com.sjxm.sjxmrpc.model.RpcResponse;
import com.sjxm.sjxmrpc.model.ServiceMetaInfo;
import com.sjxm.sjxmrpc.protocol.*;
import com.sjxm.sjxmrpc.registry.Registry;
import com.sjxm.sjxmrpc.registry.RegistryFactory;
import com.sjxm.sjxmrpc.serializer.JdkSerializer;
import com.sjxm.sjxmrpc.serializer.Serializer;
import com.sjxm.sjxmrpc.serializer.SerializerFactory;
import com.sjxm.sjxmrpc.server.tcp.VertxTcpClient;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetClient;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * 动态代理：根据要生成的对象的类型，自动生成一个代理对象
 * 常见的动态代理有：JDK动态代理、CGlib动态代理(基于字节码生成的动态代理)
 * JDK动态代理简单易用、无需引入额外的库，但缺点是只能对接口进行代理
 * CGlib动态代理更灵活、可以对任何类进行代理，但是性能略低于JDK动态代理
 */


/**
 * 服务代理（JDK 动态代理）
 */
public class ServiceProxy implements InvocationHandler {

    /**
     * 调用代理
     * @param proxy the proxy instance that the method was invoked on
     *
     * @param method the {@code Method} instance corresponding to
     * the interface method invoked on the proxy instance.  The declaring
     * class of the {@code Method} object will be the interface that
     * the method was declared in, which may be a superinterface of the
     * proxy interface that the proxy class inherits the method through.
     *
     * @param args an array of objects containing the values of the
     * arguments passed in the method invocation on the proxy instance,
     * or {@code null} if interface method takes no arguments.
     * Arguments of primitive types are wrapped in instances of the
     * appropriate primitive wrapper class, such as
     * {@code java.lang.Integer} or {@code java.lang.Boolean}.
     *
     * @return
     * @throws Throwable
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        //指定序列化器
        final Serializer serializer = SerializerFactory.getInstance(RpcApplication.getRpcConfig().getSerializer());

        //构造请求
        RpcRequest rpcRequest = RpcRequest.builder()
                .serviceName(method.getDeclaringClass().getName())
                .methodName(method.getName())
                .parameterTypes(method.getParameterTypes())
                .args(args)
                .build();

        try{
            //发送请求
            //从注册中心获取服务提供者请求地址
            RpcConfig rpcConfig = RpcApplication.getRpcConfig();
            Registry registry = RegistryFactory.getInstance(rpcConfig.getRegistryConfig().getRegistry());
            ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
            serviceMetaInfo.setServiceName(method.getDeclaringClass().getName());
            serviceMetaInfo.setServiceVersion(RpcConstant.DEFAULT_SERVICE_VERSION);
            List<ServiceMetaInfo> serviceMetaInfoList = registry.serviceDiscovery(serviceMetaInfo.getServiceKey());
            if(CollUtil.isEmpty(serviceMetaInfoList)){
                throw new RuntimeException("暂无服务地址");
            }

            //负载均衡
            LoadBalancer loadBalancer = LoadBalancerFactory.getInstance(rpcConfig.getLoadBalancer());
            //将调用方法名(请求路径)作为负载均衡参数
            Map<String,Object> requestParams = new HashMap<>();
            requestParams.put("methodName",rpcRequest.getMethodName());
            ServiceMetaInfo selectedServiceMetaInfo = loadBalancer.select(requestParams,serviceMetaInfoList);

            //发送TCP请求
            RpcResponse rpcResponse = VertxTcpClient.doRequest(rpcRequest,selectedServiceMetaInfo);
            return rpcResponse.getData();
        }catch (Exception e){
            throw new RuntimeException("调用失败");
        }
    }
}
