package server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;
import util.AESUtil;
import util.MD5Util;
import util.RSAUtil;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;

/**
 * HTTP Server Handler.
 */
public class HttpServerHandler extends SimpleChannelInboundHandler<FullHttpRequest> {
	String PRIVATE_KEY = "-----BEGIN RSA PRIVATE KEY-----\n" +
			"MIIBPgIBAAJBAMvx30Fs0OpOe+/FGJWQNtxI6OuXuBLIuMeVz34570gAkCgorB97\n" +
			"ibFZLtZjAUUK7ukAX22s2+VokBIQfgLyzgkCAwEAAQJAXpVMDxGiSigf/nEQF70M\n" +
			"VFlT/H8elUeVuqpV0pqXWXQ6VwkdcvS42wAG3AKJ02kke0heQgXcFPJLJOwK96y5\n" +
			"oQIjAO9U1695EiwD3QyoBZBBPx0pR+etJZrUFB/MDL5x6Eev/Y0CHwDaJhq7lHSk\n" +
			"NVFwso6uU0NRfqJHynXcqN1ntBKOfW0CIwCt3GuHLKO31+KoRBMulUd9LrTup4ju\n" +
			"7evkoX4Mh4EfOsktAh8AoqTSPZSbumo+RAX8tyBBCpudpmTepxwHpu/s/eupAiMA\n" +
			"v5wWrnSz4GsQ2QTG/HA9oIb+O7wFdXxtvbH433lNdlZA/Q==\n" +
			"-----END RSA PRIVATE KEY-----";

	String AES_PASSWORD = "abcdefghijklmnop";
	String AES_IV = "1111111111111111";

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest msg) throws Exception {
		String AESEncryptedData = msg.uri();

		System.out.println("---收到消息---");
		System.out.println(AESEncryptedData);

		AESUtil.keyBytes = AES_PASSWORD.getBytes(StandardCharsets.UTF_8);
		AESUtil.ivBytes = AES_IV.getBytes(StandardCharsets.UTF_8);

		byte[] AESDecryptedData = AESUtil.decryptBase64(AESEncryptedData.getBytes(StandardCharsets.UTF_8));
		String AESDecryptedDataString = new String(AESDecryptedData, StandardCharsets.UTF_8);

		System.out.println("---AES解密后---");
		System.out.println(AESDecryptedDataString);

		String[] temp = AESDecryptedDataString.split("\\|\\|");
		assert (temp.length == 2);

		String RSAEncryptedMD5Data = temp[1];
		RSAEncryptedMD5Data = RSAEncryptedMD5Data.substring(2, RSAEncryptedMD5Data.length() - 1);
		System.out.println("---RSA(MD5(Data))---");
		System.out.println(RSAEncryptedMD5Data);

		String RSADecryptedMD5Data = RSAUtil.decryptRSA(RSAEncryptedMD5Data, PRIVATE_KEY);
		System.out.println("---解密RSA(MD5(Data))，正确的话应该就是MD5(Data)---");
		System.out.println(RSADecryptedMD5Data);

		String data = temp[0];
		String MD5Data = MD5Util.toMD5(data);
		System.out.println("---MD5(Data)---");
		System.out.println(MD5Data);

		String responseString = "";
		responseString = MD5Data.equals(RSADecryptedMD5Data) ? "Correct" : "Nooooooooo";
		this.writeResponse(ctx, responseString);
	}


	/**
	 * 显示http请求的内容
	 * @param msg http请求消息
	 */
	private void displayRequest(FullHttpRequest msg) {
		System.out.println("======请求行======");
		System.out.println(msg.method() + " " + msg.uri() + " " + msg.protocolVersion());

		System.out.println("======请求头======");
		for (String name : msg.headers().names()) {
			System.out.println(name + ": " + msg.headers().get(name));

		}
		System.out.println("======消息体======");
		System.out.println(msg.content().toString(CharsetUtil.UTF_8));

	}

	/**
	 * 回复
	 * @param ctx ChannelHandlerContext
	 * @param msg 回复内容（字符串）
	 */
	private void writeResponse(ChannelHandlerContext ctx, String msg) {
		ByteBuf bf = Unpooled.copiedBuffer(msg, CharsetUtil.UTF_8);

		FullHttpResponse res = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, bf);
		res.headers().set(HttpHeaderNames.CONTENT_LENGTH, msg.length());
		ctx.writeAndFlush(res).addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
	}
}