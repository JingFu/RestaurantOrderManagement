package org.jingfu.order.bean;

import java.io.Serializable;
import java.math.BigDecimal;

public class OrderItem implements Serializable {
	private String type;
	private String name;
	private BigDecimal price;
	private int quantity;
	public OrderItem() {}
	public OrderItem(String type, String name, BigDecimal price, int quantity) {
		this.type = type;
		this.name = name;
		this.price = price;
		this.quantity = quantity;
	}
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
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
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	} 
	
}
