package com.im.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class WebSocketServer {
    //创建主从reactor模型 是无线循环事件组
    //可自定义线程的数量 默认线程数=cpu处理器 *2
    private  EventLoopGroup bossGroup = new NioEventLoopGroup(1);
    private  EventLoopGroup workGroup = new NioEventLoopGroup();
    private Channel channel=null;
    @Autowired
    private WebSocketChannelInitializer webSocketChannelInitializer;
    public ChannelFuture webSocketServer(int port) {
        //服务端启动对象
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        //设置相关参数
        serverBootstrap.group(bossGroup, workGroup)
                //声明通道类型
                .channel(NioServerSocketChannel.class)
                //设置当前通道处理器 使用日志打印处理
                .handler(new LoggingHandler(LogLevel.INFO))
                //当连接阻塞时 放入队列中进行等待
                .option(ChannelOption.SO_BACKLOG, 128)
                //设置连接保持活跃状态
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .childHandler(webSocketChannelInitializer);

        System.out.println("客户端初始化完成");
        //设置启动端口
        ChannelFuture future=null;
        try {
            //设置为异步启动
             future = serverBootstrap.bind(port).sync();
             channel = future.channel();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return future;
    }
    public void destory(){
        if (channel!=null){
            try {
                channel.closeFuture().sync();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }finally {
                //关闭事件组
                bossGroup.shutdownGracefully();
                workGroup.shutdownGracefully();
            }
        }
    }
    }

