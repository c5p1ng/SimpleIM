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
	 * tcp��·�����ɹ������
	 */
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		this.ctx = ctx;
		System.err.println("�пͻ������ӣ�" + ctx.channel().remoteAddress().toString());
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		System.err.println("channelInactive");
		super.channelInactive(ctx);
	}

	/**
	 * ������Ϣ
	 */
	public boolean sendMsg(IMMessage msg) throws IOException {
		System.err.println("������������Ϣ��" + msg);
		ctx.writeAndFlush(msg);
		return msg.getMsg().equals("q") ? false : true;
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		System.err.println("���������յ���Ϣ��" + msg);
		IMMessage message = (IMMessage) msg;
		if (OnlineUser.get(message.getUid()) == null) {
			OnlineUser.put(message.getUid(), ctx);
		}
		ChannelHandlerContext c = OnlineUser.get(message.getReceiveId());
		if (c == null) {
			message.setMsg("�Է������ߣ�");
			OnlineUser.get(message.getUid()).writeAndFlush(message);
		} else {
			c.writeAndFlush(message);
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		System.err.println("��ͻ��˶Ͽ�����:" + cause.getMessage());
		cause.printStackTrace();
		ctx.close();
	}

	@Override
	public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
		System.err.println("�����Handler����...");
		super.handlerAdded(ctx);
	}

}
