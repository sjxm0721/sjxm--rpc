package com.sjxm.sjxmrpc.model;

import cn.hutool.core.util.StrUtil;
import lombok.Data;

/**
 * 服务元信息(注册信息)
 */
@Data
public class ServiceMetaInfo {

    /**
     * 服务名称
     */
    private String serviceName;

    /**
     * 服务版本号
     */
    private String serviceVersion = "1.0";

    /**
     * 服务地址
     */
    private String serviceAddress;

    /**
     * 主机名
     */
    private String serviceHost;

    /**
     * 端口号
     */
    private String servicePort;

    /**
     * 服务分组(暂未实现)
     */
    private String serviceGroup = "default";

    /**
     * 获取服务键名
     * @return
     */
    public String getServiceKey(){
        //后续可扩展服务分组
        return String.format("%s:%s",serviceName,serviceVersion);
    }

    /**
     * 获取服务注册节点键名
     * @return
     */
    public String getServiceNodeKey(){
        return String.format("%s:%s",getServiceKey(),serviceAddress);
    }


    public String getServiceAddress(){
        if(!StrUtil.contains(serviceHost,"http")){
            return String.format("http://%s:%s",serviceHost,servicePort);
        }

        return String.format("%s:%s",serviceHost,servicePort);
    }
}
