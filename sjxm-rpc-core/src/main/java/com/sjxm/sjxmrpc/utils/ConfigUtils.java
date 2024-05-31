package com.sjxm.sjxmrpc.utils;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.setting.dialect.Props;

import java.util.Objects;

/**
 * 配置工具类
 */
public class ConfigUtils {

    private static final String[] suffixes= {".yaml",".yml",".properties"};

    /**
     * 加载配置对象
     * @param tClass
     * @param prefix
     * @return
     * @param <T>
     */
    public static <T>  T loadConfig(Class<T> tClass, String prefix) {
        T t = loadConfig(tClass, prefix, "");
        return t;
    }

    public static <T> T loadConfig(Class<T> tClass, String prefix, String environment) {
        StringBuilder configFileBuilder = new StringBuilder("application");
        if(StrUtil.isNotBlank(environment)){
            configFileBuilder.append("-").append(environment);
        }
        for(String suffix:suffixes){
            String finalPath = configFileBuilder + suffix;
            if(!FileUtil.exist(finalPath))continue;
            Props props = new Props(configFileBuilder + suffix);
            if(finalPath.endsWith(".yaml") || finalPath.endsWith(".yml")){
                if(!props.isEmpty()){
                    return props.toBean(tClass);
                }
            }
            else{
                if(!props.isEmpty()){
                    return props.toBean(tClass,prefix);
                }
            }
        }
        return null;
    }
}
