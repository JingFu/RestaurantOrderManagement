package org.jingfu.order.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.jingfu.order.assembler.OrderAssembler;
import org.jingfu.order.enums.DishStatusEnum;
import org.jingfu.order.enums.OrderStatusEnum;
import org.jingfu.order.model.AuthUser;
import org.jingfu.order.model.ProcessingDish;
import org.jingfu.order.model.ProcessingOrder;
import org.jingfu.order.util.HibernateUtil;
import org.jingfu.order.vo.DishVO;
import org.jingfu.order.vo.OrderVO;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Scope("singleton")
public class SessionManager {
	private Session session;

	public SessionManager() {
		System.out.println("===================================");
		System.out.println("Session Manager initialized");
		System.out.println("===================================");
	}

	public List<AuthUser> getAllUserInfo() {
		session = HibernateUtil.getSessionfactory().openSession();
		session.beginTransaction();
		List result = session.createQuery("from AuthUser").list();
		session.getTransaction().commit();
		session.close();
		return (List<AuthUser>) result;
	}

	public void createUser(AuthUser user) {
		session = HibernateUtil.getSessionfactory().openSession();
		session.beginTransaction();
		session.save(user);
		session.getTransaction().commit();
		session.close();
	}

	public boolean isUserExist(String userName) {
		session = HibernateUtil.getSessionfactory().openSession();
		session.beginTransaction();
		List result = session
				.createQuery("from AuthUser AU where AU.userName = :userName")
				.setParameter("userName", userName).list();
		session.getTransaction().commit();
		session.close();
		if (result != null && result.size() > 0) {
			return true;
		} else {
			return false;
		}
	}

	public void createProcessingOrder(ProcessingOrder order) {
		session = HibernateUtil.getSessionfactory().openSession();
		session.beginTransaction();
		session.save(order);
		session.getTransaction().commit();
		session.close();
	}

	public List<ProcessingOrder> getWaitingAndProcessingOrders() {
		List<ProcessingOrder> orders = new ArrayList<ProcessingOrder>();
		session = HibernateUtil.getSessionfactory().openSession();
		session.beginTransaction();
		List result = session
				.createQuery(
						"from ProcessingOrder PO where PO.status = :waitingStatus or PO.status = :processingStatus")
				.setParameter("waitingStatus",
						OrderStatusEnum.WAITING.getName())
				.setParameter("processingStatus",
						OrderStatusEnum.PROCESSING.getName()).list();
		session.getTransaction().commit();
		if (result != null && result.size() > 0) {
			for (Object o : result) {
				ProcessingOrder order = (ProcessingOrder) o;
				Hibernate.initialize(order.getProcessingDishes());
				orders.add(order);
			}
		}
		session.close();
		return orders;
	}

	public List<ProcessingOrder> getOrdersById(Set<Integer> orderIds) {
		session = HibernateUtil.getSessionfactory().openSession();
		session.beginTransaction();
		List<ProcessingOrder> orders = session.createCriteria(ProcessingOrder.class).add(Restrictions.in("processingOrderId",orderIds)).list();
		for(ProcessingOrder order : orders) {
			Hibernate.initialize(order.getProcessingDishes());
		}
		session.close();
		return orders;
	}
	
	public ProcessingOrder getOrderById(int orderId) {
		session = HibernateUtil.getSessionfactory().openSession();
		session.beginTransaction();
		List<ProcessingOrder> orders = session.createCriteria(ProcessingOrder.class).add(Restrictions.eq("processingOrderId",orderId)).list();
		for(ProcessingOrder order : orders) {
			Hibernate.initialize(order.getProcessingDishes());
		}
		session.close();
		return orders.size() > 0 ? orders.get(0) : null;
	}

	public void updateOrder(ProcessingOrder order) {
		session = HibernateUtil.getSessionfactory().openSession();
		session.beginTransaction();
		session.update(order);
		session.getTransaction().commit();
		session.close();
	}

	public List<ProcessingOrder> getProcessingOrders() {
		List<ProcessingOrder> orders = new ArrayList<ProcessingOrder>();
		session = HibernateUtil.getSessionfactory().openSession();
		session.beginTransaction();
		List result = session
				.createQuery(
						"from ProcessingOrder PO where PO.status = :processingStatus")
				.setParameter("processingStatus",
						OrderStatusEnum.PROCESSING.getName()).list();
		session.getTransaction().commit();
		if (result != null && result.size() > 0) {
			for (Object o : result) {
				ProcessingOrder order = (ProcessingOrder) o;
				Hibernate.initialize(order.getProcessingDishes());
				orders.add(order);
			}
		}
		session.close();
		return orders;
	}
	
	public List<OrderVO> getReadyDishes() {
		Map<Byte, OrderVO> orderMap = new HashMap<Byte, OrderVO>();
		List<OrderVO> orders = new ArrayList<OrderVO>();
		OrderAssembler assembler = OrderAssembler.getInstance();
		session = HibernateUtil.getSessionfactory().openSession();
		session.beginTransaction();
		List results = session.createQuery("from ProcessingOrder as o inner join o.processingDishes as d where d.quantityReady > 0").list();
		Iterator iterator = results.iterator();
		while(iterator.hasNext()) {
			Object[] result = (Object[])iterator.next();
			ProcessingOrder order = (ProcessingOrder) result[0];
			ProcessingDish dish = (ProcessingDish) result[1];
			if(orderMap.containsKey(order.getTableNo())) {
				OrderVO o = orderMap.get(order.getTableNo());
				DishVO d = assembler.assembleDishVO(dish);
				d.setOrderId(o.getOrderId());
				d.setTableNo(o.getTableNo());
				o.getDishes().add(d);
			} else {
				OrderVO o = assembler.assembleOrderVO(order);
				DishVO d = assembler.assembleDishVO(dish);
				d.setOrderId(o.getOrderId());
				d.setTableNo(o.getTableNo());
				o.getDishes().add(d);
				orderMap.put(o.getTableNo(), o);
			}
		}
		session.close();
		Collection orderCollection =  orderMap.values();
		Iterator ite = orderCollection.iterator();
		while(ite.hasNext()) {
			OrderVO order = (OrderVO) ite.next();
			orders.add(order);
		}
		Collections.sort(orders);
		return orders;
	} 
	
	public List<OrderVO> getDishAndOrderByStatus(DishStatusEnum status) {
		Map<Integer, OrderVO> orderMap = new HashMap<Integer, OrderVO>();
		List<OrderVO> orders = new ArrayList<OrderVO>();
		OrderAssembler assembler = OrderAssembler.getInstance();
		session = HibernateUtil.getSessionfactory().openSession();
		session.beginTransaction();
		List results = null;
		switch(status) {
			case WAITING: 
				results = session.createQuery("from ProcessingOrder as o inner join o.processingDishes as d where d.quantityWaiting > 0").list();
				break;
			case PROCESSING:
				results = session.createQuery("from ProcessingOrder as o inner join o.processingDishes as d where d.quantityProcessing > 0").list();
				break;
			case READY:
				results = session.createQuery("from ProcessingOrder as o inner join o.processingDishes as d where d.quantityReady > 0").list();
				break;
			default:
				break;
		}
		if(results == null) {
			session.close();
			return orders;
		}
		Iterator iterator = results.iterator();
		while(iterator.hasNext()) {
			Object[] result = (Object[])iterator.next();
			ProcessingOrder order = (ProcessingOrder) result[0];
			ProcessingDish dish = (ProcessingDish) result[1];
			if(orderMap.containsKey(order.getProcessingOrderId())) {
				OrderVO o = orderMap.get(order.getProcessingOrderId());
				DishVO d = assembler.assembleDishVO(dish);
				d.setOrderId(o.getOrderId());
				d.setTableNo(o.getTableNo());
				o.getDishes().add(d);
			} else {
				OrderVO o = assembler.assembleOrderVO(order);
				DishVO d = assembler.assembleDishVO(dish);
				d.setOrderId(o.getOrderId());
				d.setTableNo(o.getTableNo());
				o.getDishes().add(d);
				orderMap.put(o.getOrderId(), o);
			}
		}
		session.close();
		Collection orderCollection =  orderMap.values();
		Iterator ite = orderCollection.iterator();
		while(ite.hasNext()) {
			OrderVO order = (OrderVO) ite.next();
			orders.add(order);
		}
		Collections.sort(orders);
		return orders;
	}

	public ProcessingOrder searchOrderByTableNo(byte tableNo) {
		ProcessingOrder order = null;
		session = HibernateUtil.getSessionfactory().openSession();
		session.beginTransaction();
		List<ProcessingOrder> result = session
				.createQuery(
						"from ProcessingOrder PO where PO.tableNo = :tableNo order by PO.createTime desc")
				.setParameter("tableNo",
						tableNo).list();
		session.getTransaction().commit();
		if (result != null && result.size() > 0) {
			order = result.get(0);
			Hibernate.initialize(order.getProcessingDishes());
		}
		session.close();
		return order;
	}
}
