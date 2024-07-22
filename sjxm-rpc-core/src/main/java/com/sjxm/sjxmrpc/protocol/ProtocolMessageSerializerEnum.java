package com.sjxm.sjxmrpc.protocol;

import cn.hutool.core.util.ObjectUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: 四季夏目
 * @Date: 2024/7/22
 * @Description:
 */

/**
 * 协议消息的序列化器枚举
 */
@Getter
@AllArgsConstructor
public enum ProtocolMessageSerializerEnum {
    JDK(0,"jdk"),
    JSON(1,"json"),
    KRYO(2,"kryo"),
    HESSIAN(3,"hessian");

    private final int key;

    private final String value;

    /**
     * 获取值列表
     * @return
     */
    public static List<String> getValues(){
        return Arrays.stream(values()).map(item->item.getValue()).collect(Collectors.toList());
    }

    /**
     * 根据key获取枚举
     * @param key
     * @return
     */
    public static ProtocolMessageSerializerEnum getEnumByKey(int key){
        for (ProtocolMessageSerializerEnum anEnum : ProtocolMessageSerializerEnum.values()) {
            if(anEnum.key == key){
                return anEnum;
            }
        }
        return null;
    }

    /**
     * 根据value获取枚举
     * @param value
     * @return
     */
    public static ProtocolMessageSerializerEnum getEnumByValue(String value){
        if(ObjectUtil.isEmpty(value)){
            return null;
        }

        for (ProtocolMessageSerializerEnum anEnum : ProtocolMessageSerializerEnum.values()) {
            if(anEnum.value.equals(value)){
                return anEnum;
            }
        }
        return null;
    }

}
