package com.lslin.connector;

public class NettyMessage {
	private byte[] data;

	public NettyMessage(byte[] data) {
		this.data = data;
	}

	public byte[] getBytes() {
		return this.data;
	}
}
