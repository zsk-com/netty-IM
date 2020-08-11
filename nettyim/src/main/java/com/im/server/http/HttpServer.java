package com.im.server.http;

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

/**
 * 提供http服务启动类httpserver
 */
@Component
public class HttpServer {
    private EventLoopGroup bossGroup = new NioEventLoopGroup(1);
    // 默认创建的线程数量  = CPU处理器数量 * 2
    private EventLoopGroup workerGroup = new NioEventLoopGroup();

    private Channel channel;
    @Autowired
    private MyHttpInitializer initializer;

    public ChannelFuture server(int port) {
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .handler(new LoggingHandler(LogLevel.INFO))
                // 当连接被阻塞时  BACKLOG代表的是 阻塞队列的长度
                .option(ChannelOption.SO_BACKLOG, 128)
                // 设置连接为保持活动的状态
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .childHandler(initializer);
        ChannelFuture future = null;
        try {
            future = serverBootstrap.bind(port).sync();
            channel = future.channel();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {

        }
        return future;
    }

    public void close() {
        if (channel != null) {
            channel.close();
        }
        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
    }
}
