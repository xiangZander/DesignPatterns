package com.core.zander.pattern.decorator;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 需求：创建一个 InputStream 的装饰者类，把输入流中的所有大写字符转成小写。
 * @author zander.zhang
 *
 */
public class LowerCaseInputStream extends FilterInputStream {

	protected LowerCaseInputStream(InputStream in) {
		super(in);
	}
	
	@Override
	public int read() throws IOException {
		int c = super.read();
		return (c == -1 ? c : Character.toLowerCase((char)c));
	}
	
	@Override
	public int read(byte[] b, int off, int len) throws IOException {
		int result = super.read(b, off, len);
		for (int i = off; i < off + result; i++) {
			b[i] = (byte) Character.toLowerCase((char)b[i]);
		}
		return super.read(b, off, len);
	}
}
