package com.sjxm.example.consumer;

import com.sjxm.example.common.model.User;
import com.sjxm.example.common.service.UserService;
import com.sjxm.sjxmrpc.proxy.ServiceProxyFactory;

public class EasyConsumerExample {
    public static void main(String[] args) {
        // todo 需要获取UserService 的实现类对象 rpc
        UserService userService = ServiceProxyFactory.getProxy(UserService.class);
        User user = new User();
        user.setName("张三");
        //调用
        User newUser = userService.getUser(user);
        if(newUser != null) {
            System.out.println(newUser.getName());
        }
        else{
            System.out.println("user == null");
        }
    }
}
