package org.jingfu.order.assembler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jingfu.order.enums.DishStatusEnum;
import org.jingfu.order.model.ProcessingDish;
import org.jingfu.order.model.ProcessingOrder;
import org.jingfu.order.vo.DishOrderSummary;
import org.jingfu.order.vo.DishProcessingVO;
import org.jingfu.order.vo.DishVO;
import org.jingfu.order.vo.OrderVO;

public class OrderAssembler {
	private static final OrderAssembler instance = new OrderAssembler();
	private OrderAssembler() {
		
	}
	public static OrderAssembler getInstance() {
		return instance;
	}
	public DishVO assembleDishVO(ProcessingDish dish) {
		DishVO d = new DishVO();
		d.setDishId(dish.getProcessingDishId());
		d.setDishName(dish.getDishName());
		d.setDishType(dish.getDishType());
		d.setQuantity(dish.getQuantity());
		d.setQuantityReady(dish.getQuantityReady());
		d.setQuantityProcessing(dish.getQuantityProcessing());
		d.setQuantityWaiting(dish.getQuantityWaiting());
		d.setUnitPrice(dish.getUnitPrice());
		return d;
	}
	
	public OrderVO assembleOrderVO(ProcessingOrder order) {
		OrderVO o = new OrderVO();
		o.setOrderId(order.getProcessingOrderId());
		o.setTableNo(order.getTableNo());
		o.setStatus(order.getStatus());
		o.setCreateTime(order.getCreateTime());
		return o;
	}
	
	public List<DishVO> assembleDishesfromOrders(List<OrderVO> orders) {
		List<DishVO> dishes = new ArrayList<DishVO>();
		int priority = 0;
		for(OrderVO order : orders) {
			boolean setPriority = false;
			for(DishVO dish : order.getDishes()) {
				if(!setPriority) {
					priority++;
					setPriority = true;
				}
				dish.setPriority(priority);
				dishes.add(dish);
			}
		}
		return dishes;
	}
	
	public List<DishOrderSummary> assembleSummariesFromDishes(
			List<DishVO> dishes, DishStatusEnum status) {
		Map<String, DishOrderSummary> orderSummaryMap = new HashMap<String, DishOrderSummary>();
		for(DishVO dish : dishes) {
			if(orderSummaryMap.containsKey(dish.getDishName())) {
				DishOrderSummary summary = orderSummaryMap.get(dish.getDishName());
				summary.setPriority(summary.getPriority() > dish.getPriority() ? dish.getPriority() : summary.getPriority());
				summary.setQuantity(summary.getQuantity() + getSpecificQuantity(dish, status));
				summary.getDetails().add(dish);
			} else {
				DishOrderSummary summary = new DishOrderSummary();
				summary.setDishName(dish.getDishName());
				summary.setDishType(dish.getDishType());
				summary.setPriority(dish.getPriority());
				summary.setQuantity(getSpecificQuantity(dish, status));
				summary.getDetails().add(dish);
				orderSummaryMap.put(summary.getDishName(), summary);
			}
		}
		Collection<DishOrderSummary> summaries = orderSummaryMap.values();
		if(summaries instanceof List) {
			return (List<DishOrderSummary>) summaries;
		} else {
			return new ArrayList<DishOrderSummary>(summaries);
		}
	}
	
	private int getSpecificQuantity(DishVO dish, DishStatusEnum status) {
		switch(status) {
		case WAITING:
			return dish.getQuantityWaiting();
		case PROCESSING:
			return dish.getQuantityProcessing();
		case READY:
			return dish.getQuantityReady();
		default: 
			return 0;
		}
	}
	
	public Map<Integer, List<DishProcessingVO>> assembleOrderProcessingVO(
			Map<String, List<DishOrderSummary>> orderProcessingMap, DishStatusEnum status) {
		Map<Integer, List<DishProcessingVO>> dishProcessingMap = new HashMap<Integer, List<DishProcessingVO>>();
		for(List<DishOrderSummary> summaryList : orderProcessingMap.values()) {
			for(DishOrderSummary summary : summaryList) {
				if(summary.isProcessAll()) {
					for(DishVO dish : summary.getDetails()) {
						addDishToOrderVO(dishProcessingMap, dish, summary.getQuantity());
					}
				} else if(summary.getProcessQuantity() > 0){
					int quantity = summary.getProcessQuantity();
					int i = 0;
					do {
						DishVO dish = summary.getDetails().get(i);
						int processingQuantity = getSpecificQuantity(dish, status) <= quantity ? getSpecificQuantity(dish, status) : quantity; 
						addDishToOrderVO(dishProcessingMap, dish, processingQuantity);
						quantity = quantity - getSpecificQuantity(dish, status);
						i++;
					} while(quantity > 0);
				}
			}
		}
		return dishProcessingMap;
	}
	
	private void addDishToOrderVO(
			Map<Integer, List<DishProcessingVO>> dishProcessingMap,
			DishVO detail, int processingQuantity) {
		if(dishProcessingMap.containsKey(detail.getOrderId())) {
			List<DishProcessingVO> dishVOs = dishProcessingMap.get(detail.getOrderId());
			DishProcessingVO dishVO = assembleDishProcessingVO(detail, processingQuantity);
			dishVOs.add(dishVO);
		} else {
			List<DishProcessingVO> dishVOs = new ArrayList<DishProcessingVO>();
			DishProcessingVO dishVO = assembleDishProcessingVO(detail, processingQuantity);
			dishVOs.add(dishVO);
			dishProcessingMap.put(detail.getOrderId(), dishVOs);
		}
		
	}
	/*
	private void addDishToOrderVO(
	Map<Integer, List<DishProcessingVO>> dishProcessingMap, DishVO detail) {
		if(dishProcessingMap.containsKey(detail.getOrderId())) {
			List<DishProcessingVO> dishVOs = dishProcessingMap.get(detail.getOrderId());
			DishProcessingVO dishVO = assembleDishProcessingVO(detail);
			dishVOs.add(dishVO);
		} else {
			List<DishProcessingVO> dishVOs = new ArrayList<DishProcessingVO>();
			DishProcessingVO dishVO = assembleDishProcessingVO(detail);
			dishVOs.add(dishVO);
			dishProcessingMap.put(detail.getOrderId(), dishVOs);
		}

	}
	
	private DishProcessingVO assembleDishProcessingVO(DishVO detail) {
		DishProcessingVO dishVO = new DishProcessingVO();
		dishVO.setDishId(detail.getDishId());
		dishVO.setQuantity(detail.getQuantity());
		return dishVO;
	}
	*/
	private DishProcessingVO assembleDishProcessingVO(DishVO detail, int processingQuantity) {
		DishProcessingVO dishVO = new DishProcessingVO();
		dishVO.setDishId(detail.getDishId());
		dishVO.setQuantity(processingQuantity);
		return dishVO;
	}

}
