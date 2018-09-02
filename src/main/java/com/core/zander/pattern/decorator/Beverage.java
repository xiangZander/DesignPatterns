package com.core.zander.pattern.decorator;

/**
 * Header First 设计模式 - 装饰者模式 （抽象组件）
 * @author zhangxiang
 *
 */
public abstract class Beverage {
	String description = "Unknown Beverage";
	
	public String getDescription() {
		return description;
	}
	
	public abstract double cost();
}
