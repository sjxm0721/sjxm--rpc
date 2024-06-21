package com.sjxm.sjxmrpc.registry;


import com.sjxm.sjxmrpc.spi.SpiLoader;

/**
 * 注册中心接口（用于获取注册中心对象）
 */
public class RegistryFactory {
    static{
        SpiLoader.load(Registry.class);
    }
    /**
     * 默认注册中心
     */
    private static final Registry DEFAULT_REGISTRY = new EtcdRegistry();

    /**
     * 获取实例
     * @param key
     * @return
     */
    public static Registry getInstance(String key){
        return SpiLoader.getInstance(Registry.class, key);
    }
}
