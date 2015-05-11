package org.jingfu.order.enums;

public enum UserRoleEnum {
	ADMIN("ADMIN"),
	CHEF("CHEF"),
	WAITING_STAFF("WAITING_STAFF"),
	MANAGER("MANAGER");
	
	private String name;
	
	private UserRoleEnum(String name) {
		this.name = name;
	}
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public static UserRoleEnum findEnumByName(String name) {
		for(UserRoleEnum role : UserRoleEnum.values()) {
			if(role.getName().equals(name)) {
				return role;
			}
		}
		return null;
	}
}
