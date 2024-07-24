package com.sjxm.sjxmrpc.server.tcp;

import cn.hutool.core.util.IdUtil;
import com.sjxm.sjxmrpc.RpcApplication;
import com.sjxm.sjxmrpc.model.RpcRequest;
import com.sjxm.sjxmrpc.model.RpcResponse;
import com.sjxm.sjxmrpc.model.ServiceMetaInfo;
import com.sjxm.sjxmrpc.protocol.*;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetClient;
import io.vertx.core.net.NetSocket;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * @Author: 四季夏目
 * @Date: 2024/7/22
 * @Description:
 */
public class VertxTcpClient {

    public static RpcResponse doRequest(RpcRequest rpcRequest, ServiceMetaInfo serviceMetaInfo) throws InterruptedException, ExecutionException {
        //发送TCP请求
        Vertx vertx = Vertx.vertx();
        NetClient  netClient = vertx.createNetClient();
        CompletableFuture<RpcResponse> responseFuture = new CompletableFuture<>();
        netClient.connect(serviceMetaInfo.getServicePort(),serviceMetaInfo.getServiceHost(),result ->{
            if(!result.succeeded()){
                System.out.println("Failed to connect to TCP server");
                return;
            }
            NetSocket socket = result.result();
            //发送数据
            //构造消息
            ProtocolMessage<RpcRequest> protocolMessage = new ProtocolMessage<>();
            ProtocolMessage.Header header = new ProtocolMessage.Header();
            header.setMagic(ProtocolConstant.PROTOCOL_MAGIC);
            header.setVersion(ProtocolConstant.PROTOCOL_VERSION);
            header.setSerializer((byte) ProtocolMessageSerializerEnum.getEnumByValue(RpcApplication.getRpcConfig().getSerializer()).getKey());
            header.setType((byte) ProtocolMessageTypeEnum.REQUEST.getKey());
            //生成全局请求ID
            header.setRequestId(IdUtil.getSnowflakeNextId());
            protocolMessage.setHeader(header);
            protocolMessage.setBody(rpcRequest);

            //编码请求
            try{
                Buffer encodeBuffer = ProtocolMessageEncoder.encode(protocolMessage);
                socket.write(encodeBuffer);
            }catch (IOException e){
                throw new RuntimeException("协议消息编码错误");
            }

            //接收响应
            TcpBufferHandlerWrapper bufferHandlerWrapper = new TcpBufferHandlerWrapper(buffer -> {
                try{
                    ProtocolMessage<RpcResponse> responseProtocolMessage = (ProtocolMessage<RpcResponse>) ProtocolMessageDecoder.decode(buffer);
                    responseFuture.complete(responseProtocolMessage.getBody());
                }catch (IOException e){
                    throw new RuntimeException("协议消息解码错误");
                }
            });
            socket.handler(bufferHandlerWrapper);
        });

        RpcResponse rpcResponse = responseFuture.get();
        //记得关闭连接
        netClient.close();
        return rpcResponse;
    }

}
