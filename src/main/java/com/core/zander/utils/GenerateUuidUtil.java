package com.core.zander.utils;

import java.util.UUID;

public class GenerateUuidUtil {
	public static String getUUID() {
		return UUID.randomUUID().toString().replaceAll("-", "");
	}
	
	public static void main(String[] args) {
		System.out.println(getUUID());
		System.out.println(getUUID().length());
	}
}
