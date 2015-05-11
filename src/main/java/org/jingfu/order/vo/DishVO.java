package org.jingfu.order.vo;

import java.math.BigDecimal;

public class DishVO {
	private int dishId;
	private String dishName;
	private String dishType;
	private int quantity;
	private int quantityReady;
	private int quantityWaiting;
	private int quantityProcessing;
	private String status;
	private BigDecimal unitPrice;
	private int orderId;
	private int priority;
	private byte tableNo;
	public int getDishId() {
		return dishId;
	}
	public void setDishId(int dishId) {
		this.dishId = dishId;
	}
	public String getDishName() {
		return dishName;
	}
	public void setDishName(String dishName) {
		this.dishName = dishName;
	}
	public String getDishType() {
		return dishType;
	}
	public void setDishType(String dishType) {
		this.dishType = dishType;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	
	public int getQuantityReady() {
		return quantityReady;
	}
	public void setQuantityReady(int quantityReady) {
		this.quantityReady = quantityReady;
	}
	
	public int getQuantityWaiting() {
		return quantityWaiting;
	}
	public void setQuantityWaiting(int quantityWaiting) {
		this.quantityWaiting = quantityWaiting;
	}
	public int getQuantityProcessing() {
		return quantityProcessing;
	}
	public void setQuantityProcessing(int quantityProcessing) {
		this.quantityProcessing = quantityProcessing;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public BigDecimal getUnitPrice() {
		return unitPrice;
	}
	public void setUnitPrice(BigDecimal unitPrice) {
		this.unitPrice = unitPrice;
	}
	public int getOrderId() {
		return orderId;
	}
	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}
	public int getPriority() {
		return priority;
	}
	public void setPriority(int priority) {
		this.priority = priority;
	}
	public byte getTableNo() {
		return tableNo;
	}
	public void setTableNo(byte tableNo) {
		this.tableNo = tableNo;
	}
	
	
	
}
