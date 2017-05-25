package com.c5p1ng.client.handler;

import java.io.IOException;

import com.c5p1ng.client.Client;
import com.c5p1ng.client.config.BaseConfig;
import com.c5p1ng.common.IMMessage;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ClientHandler extends ChannelInboundHandlerAdapter implements BaseConfig {
	private ChannelHandlerContext ctx;

	/**
	 * tcp链路建立成功后调用
	 */
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		System.out.println(Client.UID + ":成功连接服务器");
		this.ctx = ctx;
		IMMessage message = new IMMessage(APP_IM, CLIENT_VERSION, Client.UID, MSG_LOGIN, SERVER_ID, EMPTY_MSG);
	}

	/**
	 * 发送消息
	 * 
	 * @param msg
	 * @return
	 * @throws IOException
	 */
	public boolean sendMsg(IMMessage msg) throws IOException {
		System.out.println("client:" + msg);
		ctx.channel().writeAndFlush(msg);
		return msg.getMsg().equals("q") ? false : true;
	}

	/**
	 * 收到消息后调用
	 * 
	 * @throws IOException
	 */
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws IOException {
		IMMessage m = (IMMessage) msg;
		System.out.println(m.getUid() + ":" + m.getMsg());
	}

	/**
	 * 发生异常时调用
	 */
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		System.err.println("与服务器断开连接:" + cause.getMessage());
		ctx.close();
	}
}
