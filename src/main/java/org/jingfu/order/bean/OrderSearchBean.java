package org.jingfu.order.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.jingfu.order.model.ProcessingDish;
import org.jingfu.order.service.OrderService;

@ManagedBean(name="orderSearch") // or @Named("user")
@ViewScoped
public class OrderSearchBean implements Serializable  {
	private static final long serialVersionUID = 1L;
	@ManagedProperty(value="#{orderService}")
    private OrderService orderService;
	private byte tableNo;
	private String noSesultMessage = null;
	private List<ProcessingDish> dishes = new ArrayList<ProcessingDish>();
	public byte getTableNo() {
		return tableNo;
	}
	public void setTableNo(byte tableNo) {
		this.tableNo = tableNo;
	}
	public List<ProcessingDish> getDishes() {
		return dishes;
	}
	public void setDishes(List<ProcessingDish> dishes) {
		this.dishes = dishes;
	}
	public OrderService getOrderService() {
		return orderService;
	}
	public void setOrderService(OrderService orderService) {
		this.orderService = orderService;
	}
	public String getNoSesultMessage() {
		return noSesultMessage;
	}
	public void setNoSesultMessage(String noSesultMessage) {
		this.noSesultMessage = noSesultMessage;
	}
	
	public String search() {
		noSesultMessage = null;
		dishes.clear();
		Set<ProcessingDish> results = orderService.searchByTableNo(tableNo);
		if(results == null || results.size() == 0) {
			noSesultMessage = "No Result found";
		} else {
			dishes.addAll(results);
		}
		
		return "";
	}
	
	
}