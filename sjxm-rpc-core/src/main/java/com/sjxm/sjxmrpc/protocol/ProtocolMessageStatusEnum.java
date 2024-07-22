package com.sjxm.sjxmrpc.protocol;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author: 四季夏目
 * @Date: 2024/7/22
 * @Description: 协议消息的状态枚举
 */
@Getter
@AllArgsConstructor
public enum ProtocolMessageStatusEnum {

    OK("ok",20),
    BAD_REQUEST("badRequest",40),
    BAD_RESPONSE("badResponse",50);

    private final String text;

    private final int value;


    /**
     * 根据value获取枚举
     * @param value
     * @return
     */
    public static ProtocolMessageStatusEnum getEnumByValue(int value){
        for (ProtocolMessageStatusEnum anEnum : ProtocolMessageStatusEnum.values()) {
            if(anEnum.value == value){
                return anEnum;
            }
        }
        return null;
    }
}
