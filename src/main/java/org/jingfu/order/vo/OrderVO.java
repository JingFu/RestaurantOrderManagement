package org.jingfu.order.vo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OrderVO implements Comparable<OrderVO> {
	private int orderId;
	private byte tableNo;
	private String status;
	private Date createTime;
	private List<DishVO> dishes = new ArrayList<DishVO>();
	public int getOrderId() {
		return orderId;
	}
	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}
	public byte getTableNo() {
		return tableNo;
	}
	public void setTableNo(byte tableNo) {
		this.tableNo = tableNo;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public List<DishVO> getDishes() {
		return dishes;
	}
	public void setDishes(List<DishVO> dishes) {
		this.dishes = dishes;
	}
	@Override
	public int compareTo(OrderVO o) {
		return createTime.compareTo(o.getCreateTime());
	}
	
	
}
