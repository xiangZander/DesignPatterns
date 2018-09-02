package com.core.zander.pattern.decorator;

/**
 * 装饰者模式测试类
 * @author zander.zhang
 *
 */
public class StarbuzzCoffee {

	public static void main(String[] args) {
		Beverage beverage = new HouseBlend();
		System.out.println(beverage.getDescription() + " $" + beverage.cost());
		
		Beverage beverage2 = new Espresso();
		beverage2 = new Mocha(beverage2);
		beverage2 = new Milk(beverage2);
		beverage2 = new Whip(beverage2);
		System.out.println(beverage2.getDescription() + " $" + beverage2.cost());
	}

}
