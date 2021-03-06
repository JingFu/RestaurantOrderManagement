package org.jingfu.order.enums;

public enum DishStatusEnum {
	WAITING("WAITING", "Waiting for processing"),
	PROCESSING("PROCESSING", "processing"),
	READY("READY", "ready for delivering"),
	COMPLETED("COMPLETED", "completed");
	
	private String name;
	private String description;
	
	private DishStatusEnum(String name, String description) {
		this.name = name;
		this.description = description;
	}

	public String getName() {
		return name;
	}


	public String getDescription() {
		return description;
	}

}
