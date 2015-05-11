package org.jingfu.order.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DishOrderSummary implements Serializable, Comparable<DishOrderSummary> {
	private static final long serialVersionUID = 1L;
	private String dishType;
	private String dishName;
	private int quantity;
	private int priority;
	private List<DishVO> details = new ArrayList<DishVO>();
	private boolean processAll = false;
	private int processQuantity;
	
	public String getDishType() {
		return dishType;
	}
	public void setDishType(String dishType) {
		this.dishType = dishType;
	}
	public String getDishName() {
		return dishName;
	}
	public void setDishName(String dishName) {
		this.dishName = dishName;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public int getPriority() {
		return priority;
	}
	public void setPriority(int priority) {
		this.priority = priority;
	}
	public List<DishVO> getDetails() {
		return details;
	}
	public void setDetails(List<DishVO> details) {
		this.details = details;
	}
	
	public boolean isProcessAll() {
		return processAll;
	}
	public void setProcessAll(boolean processAll) {
		this.processAll = processAll;
	}
	public int getProcessQuantity() {
		return processQuantity;
	}
	public void setProcessQuantity(int processQuantity) {
		this.processQuantity = processQuantity;
	}
	
	public boolean getFromSingleTable() {
		return details.size() == 1;
	}
	@Override
	public int compareTo(DishOrderSummary o) {
		return this.priority - o.getPriority();
	}
	
	
}
