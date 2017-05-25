package com.c5p1ng.client;

import java.io.IOException;
import java.util.Scanner;

import com.c5p1ng.client.config.BaseConfig;
import com.c5p1ng.client.handler.ClientHandler;
import com.c5p1ng.common.IMMessage;
import com.c5p1ng.common.MsgPackDecode;
import com.c5p1ng.common.MsgPackEncode;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;

public class Client implements Runnable, BaseConfig {
	public static int UID = 8889;
	public static int UID2 = 8888;
	private ClientHandler clientHandler = new ClientHandler();

	public static void main(String[] args) throws IOException {
		new Client().start();
	}

	public void start() throws IOException {
		IMMessage message = new IMMessage(APP_IM, CLIENT_VERSION, UID, MSG_LOGIN, UID2, EMPTY_MSG);
		new Thread(this).start();
		runServerCMD(message);
	}

	public void sendMsg(IMMessage msg) throws IOException {
		clientHandler.sendMsg(msg);
	}

	/** 启动客户端控制台 */
	private void runServerCMD(IMMessage message) throws IOException {
		@SuppressWarnings("resource")
		Scanner scanner = new Scanner(System.in);
		do {
			message.setMsg(scanner.nextLine());
		} while (clientHandler.sendMsg(message));
	}

	@Override
	public void run() {
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		try {
			Bootstrap b = new Bootstrap();
			b.group(workerGroup);
			b.channel(NioSocketChannel.class);
			b.option(ChannelOption.SO_KEEPALIVE, true);
			b.handler(new ChannelInitializer<SocketChannel>() {
				@Override
				public void initChannel(SocketChannel ch) throws Exception {
					ch.pipeline().addLast("frameDecoder", new LengthFieldBasedFrameDecoder(65536, 0, 2, 0, 2));
					ch.pipeline().addLast("msgpack decoder", new MsgPackDecode());
					ch.pipeline().addLast("frameEncoder", new LengthFieldPrepender(2));
					ch.pipeline().addLast("msgpack encoder", new MsgPackEncode());
					ch.pipeline().addLast(clientHandler);
				}
			});
			ChannelFuture f = b.connect(SERVER_HOST, SERVER_PORT).sync();
			f.channel().closeFuture().sync();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			workerGroup.shutdownGracefully();
		}
	}
}
