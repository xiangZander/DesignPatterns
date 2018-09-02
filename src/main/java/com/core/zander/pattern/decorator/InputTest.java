package com.core.zander.pattern.decorator;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 创建的 I/O 装饰者测试类 
 * @author zander.zhang
 *
 */
public class InputTest {

	public static void main(String[] args) {
		int c;
		try {
			InputStream in = new LowerCaseInputStream(
								 new BufferedInputStream(
								     new FileInputStream("E:/test/test.txt")));
			while ((c = in.read()) >= 0) {
				System.out.print((char) c);
			}
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
