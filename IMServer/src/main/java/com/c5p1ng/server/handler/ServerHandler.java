package com.c5p1ng.server.handler;

import java.io.IOException;

import com.c5p1ng.common.IMMessage;
import com.c5p1ng.common.OnlineUser;
import com.c5p1ng.server.config.BaseConfig;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelHandler.Sharable;

@Sharable
public class ServerHandler extends ChannelInboundHandlerAdapter implements BaseConfig {
	private ChannelHandlerContext ctx;

	/**
	 * tcp链路建立成功后调用
	 */
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		this.ctx = ctx;
		System.err.println("有客户端连接：" + ctx.channel().remoteAddress().toString());
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		System.err.println("channelInactive");
		super.channelInactive(ctx);
	}

	/**
	 * 发送消息
	 */
	public boolean sendMsg(IMMessage msg) throws IOException {
		System.err.println("服务器推送消息：" + msg);
		ctx.writeAndFlush(msg);
		return msg.getMsg().equals("q") ? false : true;
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		System.err.println("服务器接收到消息：" + msg);
		IMMessage message = (IMMessage) msg;
		if (OnlineUser.get(message.getUid()) == null) {
			OnlineUser.put(message.getUid(), ctx);
		}
		ChannelHandlerContext c = OnlineUser.get(message.getReceiveId());
		if (c == null) {
			message.setMsg("对方不在线！");
			OnlineUser.get(message.getUid()).writeAndFlush(message);
		} else {
			c.writeAndFlush(message);
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		System.err.println("与客户端断开连接:" + cause.getMessage());
		cause.printStackTrace();
		ctx.close();
	}

	@Override
	public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
		System.err.println("服务端Handler创建...");
		super.handlerAdded(ctx);
	}

}
