package org.jingfu.order.enums;

import java.math.BigDecimal;

public enum LargeSizedDimSumEnum {
	PRAWN_DUMPLING("Prawn Dumpling", BigDecimal.valueOf(5.20)),
	BEEF_SPARE_RIBS("Beef Spare Ribs", BigDecimal.valueOf(5.20)),
	LAMB_SPARE_RIBS("Lamb Spare Ribs", BigDecimal.valueOf(5.20)),
	BEEF_TENDON("Beef Tendon", BigDecimal.valueOf(5.20)),
	MINCE_PRAWN_ON_TOFU("Mince Prawn on Tofu", BigDecimal.valueOf(5.20)),
	SHANGHAI_PORK_BUN("Steamed Pork Bun in Shanghai Style", BigDecimal.valueOf(5.20)),
	SHRIMP_RICE_ROLLS("Shrimp Rice Rolls", BigDecimal.valueOf(5.20)),
	SEAWEED_SALAD("Seaweed Salad", BigDecimal.valueOf(5.20));
	
	private String name;
	private BigDecimal price;
	private LargeSizedDimSumEnum(String name, BigDecimal price) {
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
