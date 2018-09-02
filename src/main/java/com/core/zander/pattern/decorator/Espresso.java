package com.core.zander.pattern.decorator;

/**
 * 一种饮料：Espresso 浓缩咖啡 （具体组件）
 * @author zander.zhang
 *
 */
public class Espresso extends Beverage {
	
	public Espresso() {
		description = "Espresso";
	}

	@Override
	public double cost() {
		return 1.99;
	}

}
