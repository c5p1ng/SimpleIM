package com.c5p1ng.server;

import java.io.IOException;
import java.util.Scanner;

import com.c5p1ng.common.IMMessage;
import com.c5p1ng.common.MsgPackDecode;
import com.c5p1ng.common.MsgPackEncode;
import com.c5p1ng.server.config.BaseConfig;
import com.c5p1ng.server.handler.ServerHandler;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;

/**
 * 1��runServerCMD�������ڿ�������̨���룬���Ը�ָ��ID�Ŀͻ���������Ϣ�����ͷ�����������ô���ģ���
 * 2��MsgPackEncode��MsgPackDecode������Ϣ�ı���롣ʹ�õ���MessagePack��������ֽ�����С��������ٶȳ��죬ͬʱ����֧����������������ԣ������������http://msgpack.org/����
 * �������ǿ��������дʵ�����ڷ�����Ϣ�����ǵĴ��뽫�ں��������
 * 3��LengthFieldBasedFrameDecoder��LengthFieldPrepender����ΪTCP�ײ㴫������ʱ�ǲ��˽��ϲ�ҵ��ģ����Դ�����Ϣ��ʱ����������ճ��/��������
 * ��һ����������Ϣ���𿪻����������߲������Ķ�����Ϣ���ϲ���һ���͡����գ������������߾���Netty�ṩ����Ϣ���빤�ߣ�2��ʾ��Ϣ���ȣ���������ĳ���Ϊ2����2���ֽڣ���
 *
 */
public class Server implements Runnable, BaseConfig {
	ServerHandler serverHandler = new ServerHandler();

	public static void main(String[] args) throws IOException {
		new Server().start();
	}

	public void start() throws IOException {
		// ���������������Ϣ
		int toID = 1;
		IMMessage message = new IMMessage(APP_IM, CLIENT_VERSION, SERVER_ID, MSG_EMPTY, toID, EMPTY_MSG);
		new Thread(this).start();
		runServerCMD(message);
	}

	/**
	 * ��������˿���̨
	 */
	private void runServerCMD(IMMessage message) throws IOException {
		@SuppressWarnings("resource")
		Scanner scanner = new Scanner(System.in);
		do {
			message.setMsg(scanner.nextLine());
		} while (serverHandler.sendMsg(message));
	}

	@Override
	public void run() {
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		try {
			ServerBootstrap b = new ServerBootstrap();
			b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class).option(ChannelOption.SO_BACKLOG, 1024)
					.childHandler(new ChannelInitializer<SocketChannel>() {
						@Override
						public void initChannel(SocketChannel ch) throws Exception {
							ch.pipeline().addLast("frameDecoder", new LengthFieldBasedFrameDecoder(65536, 0, 2, 0, 2));
							ch.pipeline().addLast("msgpack decoder", new MsgPackDecode());
							ch.pipeline().addLast("frameEncoder", new LengthFieldPrepender(2));
							ch.pipeline().addLast("msgpack encoder", new MsgPackEncode());
							ch.pipeline().addLast(serverHandler);
						}
					});
			ChannelFuture f = b.bind(SERVER_PORT).sync();
			f.channel().closeFuture().sync();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			workerGroup.shutdownGracefully();
			bossGroup.shutdownGracefully();
		}
	}

}
