package com.sjxm.sjxmrpc.protocol;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author: 四季夏目
 * @Date: 2024/7/22
 * @Description:
 */
@Getter
@AllArgsConstructor
public enum ProtocolMessageTypeEnum {

    REQUEST(0),
    RESPONSE(1),
    HEART_BEAT(2),
    OTHERS(3);

    private final int key;

    public static ProtocolMessageTypeEnum getEnumByKey(int key){
        for (ProtocolMessageTypeEnum anEnum : ProtocolMessageTypeEnum.values()) {
            if(anEnum.key == key){
                return anEnum;
            }
        }
        return null;
    }


}
