package com.lslin.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class SelectSockets {

	public static void main(String[] args) throws IOException {
		new SelectSockets().start();
	}

	public void start() throws IOException {
		SocketChannel channel = SocketChannel.open();
		channel.connect(new InetSocketAddress("127.0.0.1", 8888));

		Selector selector = Selector.open();
		channel.configureBlocking(false);
		channel.register(selector, SelectionKey.OP_WRITE);
		while (true) {
			int readyChannels = selector.select();
			if (readyChannels == 0)
				continue;
			Set<SelectionKey> selectedKeys = selector.selectedKeys();
			Iterator<SelectionKey> keyIterator = selectedKeys.iterator();
			while (keyIterator.hasNext()) {
				SelectionKey key = keyIterator.next();
				if (key.isAcceptable()) {
					System.out.println("a connection was accepted by a ServerSocketChannel");
				} else if (key.isConnectable()) {
					System.out.println("a connection was established with a remote server");
				} else if (key.isReadable()) {
					System.out.println("a channel is ready for reading");
					SocketChannel socketChannel = (SocketChannel) key.channel();
					ByteBuffer buf = ByteBuffer.allocate(48);
					int bytesRead = socketChannel.read(buf);
					System.out.println(bytesRead + ":" + new String(buf.array()));
				} else if (key.isWritable()) {
					System.out.println("a channel is ready for writing");
				}
				keyIterator.remove();
			}
		}
	}
}
