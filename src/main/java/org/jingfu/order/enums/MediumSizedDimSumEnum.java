package org.jingfu.order.enums;

import java.math.BigDecimal;

public enum MediumSizedDimSumEnum {
	SIU_MAI("Pork Siu Mai", BigDecimal.valueOf(4.70)),
	CORIANDER_DUMPLING("Coriander Dumpling",  BigDecimal.valueOf(4.70)),
	CHICKEN_FEET("Chicken Feet",  BigDecimal.valueOf(4.70)),
	PORK_SPARE_RIBS("Pork Spare Ribs",  BigDecimal.valueOf(4.70)),
	OX_TRIPE("OX Tripe", BigDecimal.valueOf(4.70)),
	MINCE_FISH_ON_TOFU("Mince Fish on Tofu", BigDecimal.valueOf(4.70)),
	BBQ_PORK_RICE_ROLLS("BBQ Pork Rice Rolls",  BigDecimal.valueOf(4.70)),
	CHICKEN_WINGS("Deep Fried Chicken Wings",  BigDecimal.valueOf(4.70));
	
	private String name;
	private BigDecimal price;
	private MediumSizedDimSumEnum(String name, BigDecimal price) {
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
