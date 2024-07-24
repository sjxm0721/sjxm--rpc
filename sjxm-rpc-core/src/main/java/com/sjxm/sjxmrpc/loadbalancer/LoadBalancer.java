package com.sjxm.sjxmrpc.loadbalancer;

/**
 * @Author: 四季夏目
 * @Date: 2024/7/24
 * @Description:
 */

import com.sjxm.sjxmrpc.model.ServiceMetaInfo;

import java.util.List;
import java.util.Map;

/**
 * 负载均衡器(消费端使用)
 */
public interface LoadBalancer {

    ServiceMetaInfo select(Map<String,Object> requestParams, List<ServiceMetaInfo> serviceMetaInfoList);
}
