package org.jingfu.order.bean;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;

import org.jingfu.order.service.OrderService;
import org.jingfu.order.util.FacesUtil;
import org.jingfu.order.vo.DishVO;
import org.jingfu.order.vo.OrderVO;

@ManagedBean(name="orderDelivery")
@RequestScoped
public class OrderDeliveryBean {
	@ManagedProperty(value="#{orderService}")
    private OrderService orderService;
	private List<OrderVO> orders = new ArrayList<OrderVO>();

	public OrderService getOrderService() {
		return orderService;
	}

	public void setOrderService(OrderService orderService) {
		this.orderService = orderService;
	}
	
	@PostConstruct
	private void init() {
		List<OrderVO> readyOrders = orderService.getOrderWithReadyDishes();
		orders.addAll(readyOrders);
	}

	public List<OrderVO> getOrders() {
		return orders;
	}

	public void setOrders(List<OrderVO> orders) {
		this.orders = orders;
	}
	
	public void deliverItem(DishVO dish) {
		boolean isSuccess = orderService.completeDish(dish, FacesUtil.getUserName());
		if(isSuccess) {
			orders.clear();
			init();
		}
	}
	
	
}
