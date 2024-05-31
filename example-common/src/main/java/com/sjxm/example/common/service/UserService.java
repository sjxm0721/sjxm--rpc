package com.sjxm.example.common.service;


import com.sjxm.example.common.model.User;

/**
 * 用户服务
 */
public interface UserService {

    /**
     * 获取用户
     */
    User getUser(User user);

    /**
     * 获取数字
     */
    default short getNumber(){
        return 1;
    }

}
