package com.sjxm.sjxmrpc.server;


import com.sjxm.sjxmrpc.model.RpcRequest;
import com.sjxm.sjxmrpc.model.RpcResponse;
import com.sjxm.sjxmrpc.registry.LocalRegistry;
import com.sjxm.sjxmrpc.serializer.JdkSerializer;
import com.sjxm.sjxmrpc.serializer.Serializer;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;

import java.io.IOException;
import java.lang.reflect.Method;


/**
 * HTTP 请求处理
 */
public class HttpServerHandler implements Handler<HttpServerRequest> {

    @Override
    public void handle(HttpServerRequest request){
       //指定序列化器
        final Serializer serializer = new JdkSerializer();

        //记录日志
        System.out.println("Received request: "+request.method()+" "+request.uri());

        //异步处理Http请求
        request.bodyHandler(body->{
            byte[] bytes = body.getBytes();
            RpcRequest rpcRequest = null;
            try{
                rpcRequest = serializer.deserialize(bytes, RpcRequest.class);//请求体反序列化为请求rpc的java对象
            }catch (Exception e){
                e.printStackTrace();
            }

            //构建响应结果对象
            RpcResponse rpcResponse = new RpcResponse();
            //如果请求为null，直接返回
            if(rpcRequest == null){
                rpcResponse.setMessage("rpcRequest is null");
                doResponse(request,rpcResponse,serializer);
                return;
            }

            try{
                //获取要调用的服务实现类，通过反射调用
                Class<?> implClass = LocalRegistry.get(rpcRequest.getServiceName());
                Method method = implClass.getMethod(rpcRequest.getMethodName(),rpcRequest.getParameterTypes());
                Object result = method.invoke(implClass.newInstance(),rpcRequest.getArgs());
                //封装返回结果
                rpcResponse.setData(result);
                rpcResponse.setDataType(method.getReturnType());
                rpcResponse.setMessage("ok");
            }catch (Exception e){
                e.printStackTrace();
                rpcResponse.setMessage(e.getMessage());
                rpcResponse.setException(e);
            }
            //响应
            doResponse(request,rpcResponse,serializer);
        });
   }

   void doResponse(HttpServerRequest request,RpcResponse rpcResponse,Serializer serializer){
       HttpServerResponse httpServerResponse = request.response().putHeader("content-type","application/json");
       try{
           //序列化，进行网络传输返回
           byte[] serialized = serializer.serialize(rpcResponse);
           httpServerResponse.end(Buffer.buffer(serialized));
       }catch (IOException e){
           e.printStackTrace();
           httpServerResponse.end(Buffer.buffer());
       }
   }
}
