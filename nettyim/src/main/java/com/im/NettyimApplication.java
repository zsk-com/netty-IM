package com.im;

import com.im.server.WebSocketServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * CommandLineRunner 为项目启动后执行的逻辑
 */
@SpringBootApplication
public class NettyimApplication implements CommandLineRunner {
    //注入启动http启动了类
    @Autowired
    private WebSocketServer server;
    //获取配置文件中的端口
    @Value("${netty.port}")
    private int port;

    public static void main(String[] args) {
        SpringApplication.run(NettyimApplication.class, args);
    }

    /**
     * untime.getRuntime().addShutdownHook
     * 在jvm关闭时会查看是否设置addShutdownHook
     * 如果有会先执行addShutdownHook()中的逻辑在关闭
     *
     * @param args
     * @throws Exception
     */
    @Override
    public void run(String... args) throws Exception {
        server.webSocketServer(port);
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                NettyimApplication.this.server.destory();
            }
        });
    }
}
