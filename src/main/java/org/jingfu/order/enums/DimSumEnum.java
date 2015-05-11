package org.jingfu.order.enums;

public enum DimSumEnum {
	TURNIP_CAKE("101", "Turnip Cake", 4.00),
	PORK_SPRING_ROLLS("102", "Pork Spring Rolls", 4.00),
	DEEP_FRIED_MEET_DUMPLING("103", "Deep Fried Meet Dumpling", 4.00),
	SESAME_BALLS("104", "Deep Fried Sesame Balls", 4.00),
	MANGO_PUDDING("105", "Mango Pudding", 4.00),
	
	SIU_MAI("201", "Pork Siu Mai", 4.70),
	CORIANDER_DUMPLING("202", "Coriander Dumpling", 4.70),
	CHICKEN_FEET("203", "Chicken Feet", 4.70),
	PORK_SPARE_RIBS("204", "Pork Spare Ribs", 4.70),
	OX_TRIPE("205", "OX Tripe", 4.70),
	MINCE_FISH_ON_TOFU("206", "Mince Fish on Tofu", 4.70),
	BBQ_PORK_RICE_ROLLS("207", "BBQ Pork Rice Rolls", 4.70),
	CHICKEN_WINGS("208", "Deep Fried Chicken Wings", 4.70),
	
	PRAWN_DUMPLING("301", "Prawn Dumpling", 5.20),
	BEEF_SPARE_RIBS("302", "Beef Spare Ribs", 5.20),
	LAMB_SPARE_RIBS("303", "Lamb Spare Ribs", 5.20),
	BEEF_TENDON("304", "Beef Tendon", 5.20),
	MINCE_PRAWN_ON_TOFU("305", "Mince Prawn on Tofu", 5.20),
	SHANGHAI_PORK_BUN("306", "Steamed Pork Bun in Shanghai Style", 5.20),
	SHRIMP_RICE_ROLLS("307", "Shrimp Rice Rolls", 5.20),
	SEAWEED_SALAD("308", "Seaweed Salad", 5.20),
	
	KING_PRAWN_SIU_MAI("401", "King Prawn Siu Mai", 7.20),
	STICKY_RICE_WITH_CHICKEN("402", "Sticky Rice with Chicken", 7.20),
	PRAWN_SPRING_ROLLS("403", "Lamb Spare Ribs", 7.20),
	DEEP_FRIED_SQUID("404", "Deep Fried Squid", 7.20),
	STIR_FRIED_EGG_NOODLES("405", "Stir-Fried Egg Noodles in Soy Sauce", 7.20),
	
	HOT_SPICY_CRAB("501", "Hot & Spicy Crab", 11.00),
	HOT_SPICY_PRAWN("502", "Hot & Spicy Prawn", 11.00),
	BBQ_PORK("503", "BBQ Pork", 11.00),
	SWEET_SOUR_PORK("504", "Sweet & Sour Pork", 11.00),
	LEMON_CHICKEN("505", "Lemon Chicken", 11.00);
	
	private String code;
	private String description;
	private double price;
	private DimSumEnum(String code, String description, double price) {
		this.code = code;
		this.description = description;
		this.price = price;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	
	public static String findDescByCode(String code) {
		for(DimSumEnum t : DimSumEnum.values()) {
			if(t.getCode().equals(code)) {
				return t.getDescription();
			}
		}
		return null;
	}
}
