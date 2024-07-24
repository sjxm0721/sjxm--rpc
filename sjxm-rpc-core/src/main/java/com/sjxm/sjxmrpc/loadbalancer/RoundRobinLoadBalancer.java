package com.sjxm.sjxmrpc.loadbalancer;

/**
 * @Author: 四季夏目
 * @Date: 2024/7/24
 * @Description:
 */

import com.sjxm.sjxmrpc.model.ServiceMetaInfo;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 轮询负载均衡器
 */
public class RoundRobinLoadBalancer implements LoadBalancer{

    /**
     * 当前轮询的下标
     */
    private final AtomicInteger currentIndex = new AtomicInteger(0);

    @Override
    public ServiceMetaInfo select(Map<String, Object> requestParams, List<ServiceMetaInfo> serviceMetaInfoList) {
        if(serviceMetaInfoList.isEmpty()){
            return null;
        }
        //只有一个服务，无需轮询
        int size = serviceMetaInfoList.size();
        if(size == 1){
            return serviceMetaInfoList.get(0);
        }

        int index = currentIndex.getAndIncrement()%size;
        return serviceMetaInfoList.get(index);
    }
}
