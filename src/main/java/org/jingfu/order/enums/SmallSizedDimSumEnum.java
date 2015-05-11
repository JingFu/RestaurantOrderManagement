package org.jingfu.order.enums;

import java.math.BigDecimal;

public enum SmallSizedDimSumEnum {
	TURNIP_CAKE("Turnip Cake", BigDecimal.valueOf(4.00)),
	PORK_SPRING_ROLLS("Pork Spring Rolls", BigDecimal.valueOf(4.00)),
	DEEP_FRIED_MEET_DUMPLING("Deep Fried Meet Dumpling", BigDecimal.valueOf(4.00)),
	SESAME_BALLS("Deep Fried Sesame Balls", BigDecimal.valueOf(4.00)),
	MANGO_PUDDING("Mango Pudding", BigDecimal.valueOf(4.00));
	
	private String name;
	private BigDecimal price;
	private SmallSizedDimSumEnum(String name, BigDecimal price) {
		this.name = name;
		this.price = price;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	
}
