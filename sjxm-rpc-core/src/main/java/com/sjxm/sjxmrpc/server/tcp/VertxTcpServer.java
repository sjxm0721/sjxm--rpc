package com.sjxm.sjxmrpc.server.tcp;

import com.sjxm.sjxmrpc.server.HttpServer;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetServer;
import io.vertx.core.parsetools.RecordParser;

/**
 * @Author: 四季夏目
 * @Date: 2024/7/22
 * @Description:
 */
public class VertxTcpServer implements HttpServer {

    private byte[] handleRequest(byte[] requestData){
        return "Hello,client!".getBytes();
    }


    @Override
    public void doStart(int port) {
        //创建Vert.x实例
        Vertx vertx = Vertx.vertx();

        //创建TCP服务器
        NetServer server = vertx.createNetServer();

        server.connectHandler(socket->{
            // 处理请求
            //构造parser
            RecordParser parser = RecordParser.newFixed(8);
            parser.setOutput(new Handler<Buffer>() {
                //初始化
                int size = -1;
                //一次完整的读取(头 + 体)
                Buffer resultBuffer = Buffer.buffer();
                @Override
                public void handle(Buffer buffer) {
                   if(size == -1){
                       //读取消息体长度
                       size = buffer.getInt(4);
                       parser.fixedSizeMode(size);
                       //写入头信息到结果
                       resultBuffer.appendBuffer(buffer);
                   }else{
                       //写入体信息到结果
                       resultBuffer.appendBuffer(buffer);
                       System.out.println(resultBuffer.toString());
                       //重置一轮
                       parser.fixedSizeMode(8);
                       size = -1;
                       resultBuffer = Buffer.buffer();
                   }
                }
            });

            socket.handler(parser);
        });

        //启动TCP服务器并监听指定端口
        server.listen(port,result ->{
            if(result.succeeded()){
                System.out.println("TCP server started on port "+port);
            }else{
                System.out.println("Failed to start TCP server: "+result.cause());
            }
        });
    }

    public static void main(String[] args){
        new VertxTcpServer().doStart(8888);
    }
}
