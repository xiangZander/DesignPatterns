package com.core.zander.pattern.decorator;

/**
 * 一种饮料： House Blend Coffee （具体组件）
 * @author zander.zhang
 *
 */
public class HouseBlend extends Beverage {
	
	public HouseBlend() {
		description = "House Blend Coffee";
	}

	@Override
	public double cost() {
		return 0.89;
	}

}
