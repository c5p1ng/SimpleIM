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
 * 1、runServerCMD方法用于开启控制台输入，可以给指定ID的客户端推送消息（推送服务器就是这么来的）。
 * 2、MsgPackEncode和MsgPackDecode用于消息的编解码。使用的是MessagePack（编码后字节流特小，编解码速度超快，同时几乎支持所有主流编程语言，详情见官网：http://msgpack.org/）。
 * 这样我们可以随意编写实体用于发送消息，他们的代码将在后面给出。
 * 3、LengthFieldBasedFrameDecoder和LengthFieldPrepender：因为TCP底层传输数据时是不了解上层业务的，所以传输消息的时候很容易造成粘包/半包的情况
 * （一条完整的消息被拆开或者完整或者不完整的多条消息被合并到一起发送、接收），这两个工具就是Netty提供的消息编码工具，2表示消息长度（不是正真的长度为2，是2个字节）。
 *
 */
public class Server implements Runnable, BaseConfig {
	ServerHandler serverHandler = new ServerHandler();

	public static void main(String[] args) throws IOException {
		new Server().start();
	}

	public void start() throws IOException {
		// 服务端主动推送消息
		int toID = 1;
		IMMessage message = new IMMessage(APP_IM, CLIENT_VERSION, SERVER_ID, MSG_EMPTY, toID, EMPTY_MSG);
		new Thread(this).start();
		runServerCMD(message);
	}

	/**
	 * 启动服务端控制台
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
