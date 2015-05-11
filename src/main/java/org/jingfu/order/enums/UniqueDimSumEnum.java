package org.jingfu.order.enums;

import java.math.BigDecimal;

public enum UniqueDimSumEnum {
	HOT_SPICY_CRAB("Hot & Spicy Crab", BigDecimal.valueOf(11.00)),
	HOT_SPICY_PRAWN("Hot & Spicy Prawn", BigDecimal.valueOf(11.00)),
	BBQ_PORK("BBQ Pork", BigDecimal.valueOf(11.00)),
	SWEET_SOUR_PORK("Sweet & Sour Pork", BigDecimal.valueOf(11.00)),
	LEMON_CHICKEN("Lemon Chicken", BigDecimal.valueOf(11.00));
	
	private String name;
	private BigDecimal price;
	private UniqueDimSumEnum(String name, BigDecimal price) {
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
