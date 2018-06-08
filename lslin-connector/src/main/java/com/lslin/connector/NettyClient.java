package com.lslin.connector;

import java.net.URI;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class NettyClient {
	private Channel channel;
	private URI uri;
	private final static EventLoopGroup group = new NioEventLoopGroup();
	private Bootstrap b;

	public void initConnect(URI uri) throws Exception {
		this.uri = uri;
		if (this.channel != null)
			this.channel.close();
		this.channel = this.connect();
	}

	private Channel connect() throws Exception {
		if (this.b == null) {
			this.b = new Bootstrap();
			this.b.group(group).channel(NioSocketChannel.class).option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 1000)
					.handler(new ChannelInitializer<SocketChannel>() {
						@Override
						protected void initChannel(SocketChannel ch) {
							preparePipeline(ch.pipeline());
						}
					});
		}
		return this.b.connect(this.uri.getHost(), this.uri.getPort()).sync().channel();
	}

	private void preparePipeline(ChannelPipeline pipeline) {
		pipeline.addLast(new NettyMessageDecoder(), new NettyMessageEncoder(), new NettyMessageHandler());
	}

	public void send(NettyMessage msg) throws Exception {
		this.channel.writeAndFlush(msg);
	}
}
