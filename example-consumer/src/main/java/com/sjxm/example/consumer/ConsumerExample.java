package com.sjxm.example.consumer;

import com.sjxm.example.common.model.User;
import com.sjxm.example.common.service.UserService;
import com.sjxm.sjxmrpc.config.RpcConfig;
import com.sjxm.sjxmrpc.proxy.ServiceProxyFactory;
import com.sjxm.sjxmrpc.utils.ConfigUtils;

/**
 * 服务消费者示例
 */
public class ConsumerExample {

    public static void main(String[] args) {
//        RpcConfig rpc = ConfigUtils.loadConfig(RpcConfig.class,"rpc");
//        System.out.println(rpc);

        //获取代理
        UserService userService = ServiceProxyFactory.getProxy(UserService.class);
        User user = new User();
        user.setName("sjxm");
        //调用
        User newUser = userService.getUser(user);
        if(newUser != null) {
            System.out.println(newUser.getName());
        }else{
            System.out.println("user == null");
        }
        long number = userService.getNumber();
        System.out.println(number);
    }

}
