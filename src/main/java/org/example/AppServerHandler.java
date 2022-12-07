package org.example;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.AttributeKey;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.awt.*;
import java.net.URI;
import java.util.Date;

public class AppServerHandler extends SimpleChannelInboundHandler<ByteBuf> {


    /*private final static AttributeKey<String> USERNAME = AttributeKey.valueOf("username");*/

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf byteBuf) throws Exception {
        int packetId = byteBuf.readInt();

        if (packetId == 0) {
            var message = readString(byteBuf);

            System.out.println(message);

        } else if (packetId == 1) {
           /* if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                Desktop.getDesktop().browse(new URI("http://www.google.com"));
            }*/
            String clientUri = readString(byteBuf);
            try {
                Desktop.getDesktop().browse(new URI(clientUri));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (packetId == 2) {
            var date = new Date();
            var dateString = date.toString();

            var writeBuf = Unpooled.buffer();
            writeBuf.writeBytes(dateString.getBytes());
            ctx.channel().writeAndFlush(writeBuf);

        }
    }

    private static String readString(ByteBuf buf) {
        int length = buf.readableBytes();

        byte[] content = new byte[length];
        buf.readBytes(content, 0, length);

        return new String(content,0,length);
    }
}
