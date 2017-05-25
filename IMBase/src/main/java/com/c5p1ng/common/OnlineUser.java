package com.c5p1ng.common;

import java.util.HashMap;

import io.netty.channel.ChannelHandlerContext;

/**
 * �����û���
 */
public class OnlineUser {
	// �û���
	private static HashMap<Integer, ChannelHandlerContext> onlineUser = new HashMap<Integer, ChannelHandlerContext>();

	public static void put(Integer uid, ChannelHandlerContext uchc) {
		onlineUser.put(uid, uchc);
	}

	public static void remove(Integer uid) {
		onlineUser.remove(uid);
	}

	public static ChannelHandlerContext get(Integer uid) {
		return onlineUser.get(uid);
	}
}
