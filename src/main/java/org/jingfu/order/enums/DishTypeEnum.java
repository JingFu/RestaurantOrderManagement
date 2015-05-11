package org.jingfu.order.enums;

public enum DishTypeEnum {
	SMALL_SIZED_DISHES("SMALL_SIZED", "Small Sized Dishes"),
	MEDIUM_SIZED_DISHES("MEDIUM_SIZED", "Medium Sized Dishes"),
	LARGE_SIZED_DISHES("LARGE_SIZED", "Large Sized Dishes"),
	SPECIAL_DISHES("SPECIAL", "Special Dishes"),
	UNIQUE_DISHES("UNIQUE", "Unique Dishes");
	
	private String code;
	private String description;
	private DishTypeEnum(String code, String description) {
		this.code = code;
		this.description = description;
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
	
	public static String findDescByCode(String code) {
		for(DishTypeEnum t : DishTypeEnum.values()) {
			if(t.getCode().equals(code)) {
				return t.getDescription();
			}
		}
		return null;
	}
	
	
}
