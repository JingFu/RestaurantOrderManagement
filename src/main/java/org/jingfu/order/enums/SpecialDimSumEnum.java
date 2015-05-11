package org.jingfu.order.enums;

import java.math.BigDecimal;

public enum SpecialDimSumEnum {
	KING_PRAWN_SIU_MAI("King Prawn Siu Mai", BigDecimal.valueOf(7.20)),
	STICKY_RICE_WITH_CHICKEN("Sticky Rice with Chicken", BigDecimal.valueOf(7.20)),
	PRAWN_SPRING_ROLLS("Lamb Spare Ribs", BigDecimal.valueOf(7.20)),
	DEEP_FRIED_SQUID("Deep Fried Squid", BigDecimal.valueOf(7.20)),
	STIR_FRIED_EGG_NOODLES("Stir-Fried Egg Noodles in Soy Sauce", BigDecimal.valueOf(7.20));
	
	private String name;
	private BigDecimal price;
	private SpecialDimSumEnum(String name, BigDecimal price) {
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
