package com.sjxm.sjxmrpc.proxy;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.sjxm.sjxmrpc.RpcApplication;
import com.sjxm.sjxmrpc.config.RpcConfig;
import com.sjxm.sjxmrpc.constant.RpcConstant;
import com.sjxm.sjxmrpc.model.RpcRequest;
import com.sjxm.sjxmrpc.model.RpcResponse;
import com.sjxm.sjxmrpc.model.ServiceMetaInfo;
import com.sjxm.sjxmrpc.protocol.*;
import com.sjxm.sjxmrpc.registry.Registry;
import com.sjxm.sjxmrpc.registry.RegistryFactory;
import com.sjxm.sjxmrpc.serializer.JdkSerializer;
import com.sjxm.sjxmrpc.serializer.Serializer;
import com.sjxm.sjxmrpc.serializer.SerializerFactory;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetClient;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.List;
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
            //序列化
            byte[] bodyBytes = serializer.serialize(rpcRequest);
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

            //暂时取第一个
            ServiceMetaInfo selectedServiceMetaInfo = serviceMetaInfoList.get(0);

//            //发送HTTP请求
//            try(HttpResponse httpResponse = HttpRequest.post(selectedServiceMetaInfo.getServiceAddress()).body(bodyBytes).execute()){
//                byte[] result = httpResponse.bodyBytes();
//                //反序列化
//                RpcResponse rpcResponse = serializer.deserialize(result,RpcResponse.class);
//                return rpcResponse.getData();
//            }

            //发送TCP请求
            Vertx vertx = Vertx.vertx();
            NetClient netClient = vertx.createNetClient();
            CompletableFuture<RpcResponse> responseFuture = new CompletableFuture<>();
            netClient.connect(selectedServiceMetaInfo.getServicePort(),selectedServiceMetaInfo.getServiceHost(),result->{
                if(result.succeeded()){
                    System.out.println("Connected to TCP server");
                    io.vertx.core.net.NetSocket socket = result.result();
                    //发送消息
                    //构造消息
                    ProtocolMessage<RpcRequest> protocolMessage = new ProtocolMessage<>();
                    ProtocolMessage.Header header = new ProtocolMessage.Header();
                    header.setMagic(ProtocolConstant.PROTOCOL_MAGIC);
                    header.setVersion(ProtocolConstant.PROTOCOL_VERSION);
                    header.setSerializer((byte) ProtocolMessageSerializerEnum.getEnumByValue(RpcApplication.getRpcConfig().getSerializer()).getKey());
                    header.setType((byte) ProtocolMessageTypeEnum.REQUEST.getKey());
                    header.setRequestId(IdUtil.getSnowflakeNextId());
                    protocolMessage.setHeader(header);
                    protocolMessage.setBody(rpcRequest);
                    //编码请求
                    try{
                        Buffer encodeBuffer = ProtocolMessageEncoder.encode(protocolMessage);
                        socket.write(encodeBuffer);
                    }catch (IOException e){
                        throw new RuntimeException("协议消息编码错误");
                    }

                    //接收响应
                    socket.handler(buffer -> {
                        try{
                            ProtocolMessage<RpcResponse> rpcResponseProtocolMessage = (ProtocolMessage<RpcResponse>) ProtocolMessageDecoder.decode(buffer);

                            responseFuture.complete(rpcResponseProtocolMessage.getBody());
                        }catch (IOException e){
                            throw new RuntimeException("协议消息解码错误");
                        }
                    });
                }else{
                    System.err.println("Failed to connect to TCP server");
                }
            });

            RpcResponse rpcResponse = responseFuture.get();
            //记得关闭连接
            netClient.close();
            return rpcResponse.getData();
        }catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }
}
