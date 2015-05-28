package org.jingfu.order.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jingfu.order.assembler.OrderAssembler;
import org.jingfu.order.bean.OrderItem;
import org.jingfu.order.enums.DishStatusEnum;
import org.jingfu.order.enums.OrderStatusEnum;
import org.jingfu.order.model.ProcessingDish;
import org.jingfu.order.model.ProcessingOrder;
import org.jingfu.order.vo.DishOrderSummary;
import org.jingfu.order.vo.DishProcessingVO;
import org.jingfu.order.vo.DishVO;
import org.jingfu.order.vo.OrderVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Scope("singleton")
public class OrderService {
	@Autowired
	private SessionManager sessionManager;
	private OrderAssembler assembler = OrderAssembler.getInstance();
	
	public OrderService() {
		System.out.println("================================");
		System.out.println("OrderService Bean is initialized");
		System.out.println("================================");
	}
	
	public ProcessingOrder assembleProcessingOrder(String userName, byte tableNo,
			List<OrderItem> orderedItems) {
		ProcessingOrder order = new ProcessingOrder();
		order.setCreateBy(userName);
		order.setTableNo(tableNo);
		order.setStatus(OrderStatusEnum.WAITING.getName());
		order.setCreateTime(new Date());
		for(OrderItem item : orderedItems) {
			ProcessingDish dish = assembleProcessingDish(order, userName, item);
			order.getProcessingDishes().add(dish);
		}
		return order;
	}

	private ProcessingDish assembleProcessingDish(ProcessingOrder order, String username, OrderItem item) {
		ProcessingDish dish = new ProcessingDish();
		dish.setProcessingOrder(order);
		dish.setCreateBy(username);
		dish.setDishName(item.getName());
		dish.setDishType(item.getType());
		dish.setQuantity(item.getQuantity());
		dish.setQuantityWaiting(item.getQuantity());
		dish.setUnitPrice(item.getPrice());
		dish.setStatus(DishStatusEnum.WAITING.getName());
		dish.setCreateTime(new Date());
		return dish;
	}

	public void saveProcessingOrder(ProcessingOrder order) {
		sessionManager.createProcessingOrder(order);
		
	}

	public List<DishOrderSummary> getWaitingDishSummaries() {
		List<OrderVO> orders = sessionManager.getDishAndOrderByStatus(DishStatusEnum.WAITING);
		List<DishVO> waitingDishes = assembler.assembleDishesfromOrders(orders);
		List<DishOrderSummary> dishOrderSummaries = assembler.assembleSummariesFromDishes(waitingDishes, DishStatusEnum.WAITING);
		
		return dishOrderSummaries;
	}


	public List<ProcessingOrder> ProcessDishes(
			Map<Integer, List<DishProcessingVO>> dishProcessingMap, DishStatusEnum status, String username) {
		Set<Integer> orderIds = dishProcessingMap.keySet();
		List<ProcessingOrder> orders = sessionManager.getOrdersById(orderIds);
		updateOrderAndDishStatus(orders, dishProcessingMap, status, username);
		return orders;
	}

	private void updateOrderAndDishStatus(List<ProcessingOrder> orders,
			Map<Integer, List<DishProcessingVO>> dishProcessingMap,
			DishStatusEnum status, String username) {
		for(ProcessingOrder order : orders) {
			List<DishProcessingVO> dishProcessingVOs = dishProcessingMap.get(order.getProcessingOrderId());
			Map<Integer, DishProcessingVO> dishMap = constructDishProcessingMap(dishProcessingVOs);
			for(ProcessingDish dish : order.getProcessingDishes()) {
				DishProcessingVO dishVO = dishMap.get(dish.getProcessingDishId());
				if(dishVO != null) {
					switch(status) {
					case PROCESSING:
						SetDishToProcessing(dish, dishVO);
						break;
					case READY:
						setDishToReady(dish, dishVO);
						break;
					case COMPLETED:
						setDishToCompleted(dish, dishVO);
					default:
						break;
				}
				dish.setUpdateBy(username);
				dish.setUpdateTime(new Date());
				}
			}
			switch(status) {
			case PROCESSING:
				order.setStatus(OrderStatusEnum.PROCESSING.getName());
				break;
			case READY:
				setOrderToReady(order);
				break;
			case COMPLETED:
				setOrderToCompleted(order);
			default:
				break;
			}
			order.setUpdateBy(username);
			order.setUpdateTime(new Date());
		}
		
	}

	private void setOrderToCompleted(ProcessingOrder order) {
		boolean isCompleted = true;
		for(ProcessingDish dish : order.getProcessingDishes()) {
			if(!DishStatusEnum.COMPLETED.getName().equals(dish.getStatus())) {
				isCompleted = false;
				break;
			}
		}
		if(isCompleted) {
			order.setStatus(OrderStatusEnum.COMPLETED.getName());
		}
	}

	private void setOrderToReady(ProcessingOrder order) {
		boolean isReady = true;
		for(ProcessingDish dish : order.getProcessingDishes()) {
			if(DishStatusEnum.WAITING.getName().equals(dish.getStatus()) || DishStatusEnum.PROCESSING.getName().equals(dish.getStatus())) {
				isReady = false;
				break;
			}
		}
		if(isReady) {
			order.setStatus(OrderStatusEnum.READY.getName());
		}
	}

	private void setDishToCompleted(ProcessingDish dish, DishProcessingVO dishVO) {
		dish.setQuantityCompleted(dish.getQuantityCompleted() + dishVO.getQuantity());
		dish.setQuantityReady(dish.getQuantityReady() - dishVO.getQuantity());
		if(dish.getQuantity() == dish.getQuantityCompleted()) {
			dish.setStatus(DishStatusEnum.COMPLETED.getName());
		}
		
	}

	private void setDishToReady(ProcessingDish dish, DishProcessingVO dishVO) {
		dish.setQuantityReady(dish.getQuantityReady() + dishVO.getQuantity());
		dish.setQuantityProcessing(dish.getQuantityProcessing() - dishVO.getQuantity());
		if(dish.getQuantityWaiting() == 0 && dish.getQuantityProcessing() == 0) {
			dish.setStatus(DishStatusEnum.READY.getName());
		}
	}

	private void SetDishToProcessing(ProcessingDish dish,
			DishProcessingVO dishVO) {
		dish.setQuantityProcessing(dish.getQuantityProcessing() + dishVO.getQuantity());
		dish.setQuantityWaiting(dish.getQuantityWaiting() - dishVO.getQuantity());
		dish.setStatus(DishStatusEnum.PROCESSING.getName());
		
	}

	private Map<Integer, DishProcessingVO> constructDishProcessingMap(
			List<DishProcessingVO> dishProcessingVOs) {
		Map<Integer, DishProcessingVO> dishMap = new HashMap<Integer, DishProcessingVO>();
		for(DishProcessingVO dishVO: dishProcessingVOs) {
			dishMap.put(dishVO.getDishId(), dishVO);
		}
		return dishMap;
	}

	public void updateOrders(List<ProcessingOrder> orders) {
		for(ProcessingOrder order : orders) {
			sessionManager.updateOrder(order);
		}
		
		
	}

	public List<DishOrderSummary> getProcessingDishSummaries() {
		List<OrderVO> orders = sessionManager.getDishAndOrderByStatus(DishStatusEnum.PROCESSING);
		List<DishVO> processingDishes = assembler.assembleDishesfromOrders(orders);
		List<DishOrderSummary> dishOrderSummaries = assembler.assembleSummariesFromDishes(processingDishes, DishStatusEnum.PROCESSING);
		return dishOrderSummaries;
	}


	public List<OrderVO> getOrderWithReadyDishes() {
		return sessionManager.getReadyDishes();
	}

	public boolean completeDish(DishVO dishVO, String username) {
		ProcessingOrder order = sessionManager.getOrderById(dishVO.getOrderId());
		if(order != null) {
			for(ProcessingDish dish : order.getProcessingDishes()) {
				if(dish.getProcessingDishId() == dishVO.getDishId()) {
					DishProcessingVO dishProcessingVO = new DishProcessingVO();
					dishProcessingVO.setDishId(dishVO.getDishId());
					dishProcessingVO.setQuantity(dishVO.getQuantityReady());
					setDishToCompleted(dish, dishProcessingVO);
					dish.setUpdateBy(username);
					dish.setUpdateTime(new Date());
					break;
				}
			}
			setOrderToCompleted(order);
			order.setUpdateBy(username);
			order.setUpdateTime(new Date());
			sessionManager.updateOrder(order);
			return true;
		}
		return false;
	}

	public Set<ProcessingDish> searchByTableNo(byte tableNo) {
		ProcessingOrder order = sessionManager.searchOrderByTableNo(tableNo);
		if(order == null) {
			return null;
		} else {
			return order.getProcessingDishes();
		}
		
	}


}
