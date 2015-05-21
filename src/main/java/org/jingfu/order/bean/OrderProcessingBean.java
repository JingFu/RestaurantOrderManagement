package org.jingfu.order.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

import org.jingfu.order.assembler.OrderAssembler;
import org.jingfu.order.enums.DishStatusEnum;
import org.jingfu.order.model.ProcessingOrder;
import org.jingfu.order.service.OrderService;
import org.jingfu.order.util.FacesUtil;
import org.jingfu.order.vo.DishOrderSummary;
import org.jingfu.order.vo.DishProcessingVO;

@ManagedBean(name="orderProcessing")
@RequestScoped
public class OrderProcessingBean implements Serializable{
	@ManagedProperty(value="#{orderService}")
    private OrderService orderService;
	private OrderAssembler assembler = OrderAssembler.getInstance();
	private Map<String, List<DishOrderSummary>> orderWaitingMap = null;
	private Map<String, List<DishOrderSummary>> orderProcessingMap = null;
	private String activeTab = "waiting_tab";
	
	public OrderService getOrderService() {
		return orderService;
	}

	public void setOrderService(OrderService orderService) {
		this.orderService = orderService;
	}
	
	@PostConstruct
	private void init() {
		initOrderWaitingMap();
		initOrderProcessingMap();
	}
	
	private void initOrderWaitingMap() {
		orderWaitingMap = new TreeMap<String, List<DishOrderSummary>>(); 
		List<DishOrderSummary> orderSummaries = orderService.getWaitingDishSummaries();
		for(DishOrderSummary summary : orderSummaries) {
			if(orderWaitingMap.containsKey(summary.getDishType())) {
				orderWaitingMap.get(summary.getDishType()).add(summary);
			} else {
				List<DishOrderSummary> orderSummaryList = new ArrayList<DishOrderSummary>();
				orderSummaryList.add(summary);
				orderWaitingMap.put(summary.getDishType(), orderSummaryList);
			}
		}
		for(List<DishOrderSummary> summaryList : orderWaitingMap.values()) {
			Collections.sort(summaryList);
		}
	}
	
	private void initOrderProcessingMap() {
		orderProcessingMap = new TreeMap<String, List<DishOrderSummary>>(); 
		List<DishOrderSummary> orderSummaries = orderService.getProcessingDishSummaries();
		for(DishOrderSummary summary : orderSummaries) {
			if(orderProcessingMap.containsKey(summary.getDishType())) {
				orderProcessingMap.get(summary.getDishType()).add(summary);
			} else {
				List<DishOrderSummary> orderSummaryList = new ArrayList<DishOrderSummary>();
				orderSummaryList.add(summary);
				orderProcessingMap.put(summary.getDishType(), orderSummaryList);
			}
		}
		for(List<DishOrderSummary> summaryList : orderProcessingMap.values()) {
			Collections.sort(summaryList);
		}
	}

	public Map<String, List<DishOrderSummary>> getOrderWaitingMap() {
		return orderWaitingMap;
	}
	
	

	public void setOrderWaitingMap(
			Map<String, List<DishOrderSummary>> orderWaitingMap) {
		this.orderWaitingMap = orderWaitingMap;
	}
	
	public Map<String, List<DishOrderSummary>> getOrderProcessingMap() {
		return orderProcessingMap;
	}
	
	public void setOrderProcessingMap(
			Map<String, List<DishOrderSummary>> orderProcessingMap) {
		this.orderProcessingMap = orderProcessingMap;
	}
	
	public String startProcess() {
		if(!validate(orderWaitingMap)) {
			FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "No dish selected for processing",
                    "Please try again!"));
			activeTab = "waiting_tab";
            return "";
		}
		Map<Integer, List<DishProcessingVO>> dishProcessingMap = assembler.assembleOrderProcessingVO(orderWaitingMap, DishStatusEnum.WAITING);
		List<ProcessingOrder> orders = orderService.ProcessDishes(dishProcessingMap, DishStatusEnum.PROCESSING, FacesUtil.getUserName());
		orderService.updateOrders(orders);
		activeTab = "waiting_tab";
		orderWaitingMap = null;
		return "processing";
	}
	
	private boolean validate(
			Map<String, List<DishOrderSummary>> orderMap) {
		Collection<List<DishOrderSummary>> orderCollection = orderMap.values();
		Iterator<List<DishOrderSummary>> iterator = orderCollection.iterator();
		while(iterator.hasNext()) {
			List<DishOrderSummary> summaries = iterator.next();
			for(DishOrderSummary summary : summaries) {
				if(summary.isProcessAll() || summary.getProcessQuantity() > 0) {
					return true;
				}
			}
		}
		return false;
	}

	public String completeProcess() {
		if(!validate(orderProcessingMap)) {
			FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "No dish selected for completing",
                    "Please try again!"));
			activeTab = "processing_tab";
            return "";
		}
		Map<Integer, List<DishProcessingVO>> dishProcessingMap = assembler.assembleOrderProcessingVO(orderProcessingMap, DishStatusEnum.PROCESSING);
		List<ProcessingOrder> orders = orderService.ProcessDishes(dishProcessingMap, DishStatusEnum.READY, FacesUtil.getUserName());
		orderService.updateOrders(orders);
		activeTab = "processing_tab";
		return "processing";
	}
	
	public boolean getProcessButtonRendered() {
		return orderWaitingMap != null && orderWaitingMap.keySet().size() > 0;
	}
	
	public boolean getCompleteButtonRendered() {
		return orderProcessingMap != null && orderProcessingMap.keySet().size() > 0;
	}

	public String getActiveTab() {
		return activeTab;
	}

	public void setActiveTab(String activeTab) {
		this.activeTab = activeTab;
	}
	
	
	

}
