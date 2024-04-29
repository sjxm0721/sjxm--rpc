package com.sjxm.example.consumer;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.sjxm.example.common.model.User;
import com.sjxm.example.common.service.UserService;
import com.sjxm.sjxmrpc.model.RpcRequest;
import com.sjxm.sjxmrpc.model.RpcResponse;
import com.sjxm.sjxmrpc.serializer.JdkSerializer;
import com.sjxm.sjxmrpc.serializer.Serializer;

import java.io.IOException;


/**
 * 此处使用的是静态代理
 * 静态代理：为每一个特定类型的接口或对象，编写一个代理类
 */
public class UserServiceProxy implements UserService {
    @Override
    public User getUser(User user) {
        //指定序列化器
        Serializer serializer = new JdkSerializer();

        //发请求
        RpcRequest rpcRequest = RpcRequest.builder()
                .serviceName(UserService.class.getName())
                .methodName("getUser")
                .parameterTypes(new Class[]{User.class})
                .args(new Object[]{user})
                .build();
        try{
            byte[] bodyBytes = serializer.serialize(rpcRequest);
            byte[] result;
            try(HttpResponse httpResponse = HttpRequest.post("http://localhost:8080").body(bodyBytes).execute()){
                result = httpResponse.bodyBytes();
            }
            RpcResponse rpcResponse = serializer.deserialize(result, RpcResponse.class);
            return (User) rpcResponse.getData();
        }catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }
}
