package org.example;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class AppServer {
    private final int port;
    private final EventLoopGroup bossGroup;
    private final EventLoopGroup workGroup;

    public AppServer(int port) {
        this.port = port;
        this.bossGroup = new NioEventLoopGroup();
        this.workGroup = new NioEventLoopGroup();
    }

    public void start() {

        ServerBootstrap bootstrap = new ServerBootstrap();

        try {
            bootstrap
                    .group(bossGroup, workGroup) // checka ifall du behöver använda dig av this senare.
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addFirst(new AppServerHandler());
                        }
                    })
                    .bind(port).sync().channel().closeFuture().sync();

            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
