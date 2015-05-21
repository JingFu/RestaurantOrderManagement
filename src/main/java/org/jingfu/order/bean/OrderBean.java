package org.jingfu.order.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;
import javax.faces.validator.ValidatorException;

import org.jingfu.order.enums.DishTypeEnum;
import org.jingfu.order.enums.LargeSizedDimSumEnum;
import org.jingfu.order.enums.MediumSizedDimSumEnum;
import org.jingfu.order.enums.SmallSizedDimSumEnum;
import org.jingfu.order.enums.SpecialDimSumEnum;
import org.jingfu.order.enums.UniqueDimSumEnum;
import org.jingfu.order.model.ProcessingOrder;
import org.jingfu.order.service.OrderService;
// or import javax.inject.Named;
// or import javax.enterprise.context.SessionScoped;
import org.jingfu.order.util.FacesUtil;

@ManagedBean(name="order") // or @Named("user")
//@RequestScoped
@ViewScoped
public class OrderBean implements Serializable {
	@ManagedProperty(value="#{orderService}")
    private OrderService orderService;

	
	private byte tableNo;
	private SelectItem[] dishTypes;
	private String selectedDishType;
	private SelectItem[] dishNames;
	private String selectedDishName;
	private BigDecimal selectedDishPrice;
	private int selectedDishQuantity;
	private List<OrderItem> orderedItems = new ArrayList<OrderItem>();
//	private DataModel<OrderItem> orderItemDataModel = new ListDataModel<OrderItem>(orderedItems);
	 
	
//	public OrderBean() {
//		initOrder();
//	}

	@PostConstruct
	private void initOrder() {
		dishTypes = new SelectItem[DishTypeEnum.values().length];
		for(int i= 0; i < DishTypeEnum.values().length; i++) {
			dishTypes[i] = new SelectItem(DishTypeEnum.values()[i].getCode());
		}
		dishNames = new SelectItem[SmallSizedDimSumEnum.values().length];
		for(int i = 0; i < SmallSizedDimSumEnum.values().length; i++) {
			dishNames[i] = new SelectItem(SmallSizedDimSumEnum.values()[i].getName());
		}
		selectedDishType = DishTypeEnum.values()[0].getCode();
		selectedDishName = SmallSizedDimSumEnum.values()[0].getName();
		selectedDishPrice = SmallSizedDimSumEnum.values()[0].getPrice();
		tableNo = 1;
		selectedDishQuantity = 1;
		orderedItems.clear();
	}
	
	
	public void updateDishNamesAndPrice() {
		if (DishTypeEnum.SMALL_SIZED_DISHES.getCode().equals(selectedDishType)) {
			SelectItem[] dishNames = new SelectItem[SmallSizedDimSumEnum.values().length];
			for(int i = 0; i < SmallSizedDimSumEnum.values().length; i++) {
				dishNames[i] = new SelectItem(SmallSizedDimSumEnum.values()[i].getName());
			}
			setDishNames(dishNames);
			setSelectedDishPrice(SmallSizedDimSumEnum.values()[0].getPrice());
		} else if(DishTypeEnum.MEDIUM_SIZED_DISHES.getCode().equals(selectedDishType)) {
			SelectItem[] dishNames = new SelectItem[MediumSizedDimSumEnum.values().length];
			for(int i = 0; i < MediumSizedDimSumEnum.values().length; i++) {
				dishNames[i] = new SelectItem(MediumSizedDimSumEnum.values()[i].getName());
			}
			setDishNames(dishNames);
			setSelectedDishPrice(MediumSizedDimSumEnum.values()[0].getPrice());
		} else if(DishTypeEnum.LARGE_SIZED_DISHES.getCode().equals(selectedDishType)) {
			SelectItem[] dishNames = new SelectItem[LargeSizedDimSumEnum.values().length];
			for(int i = 0; i < LargeSizedDimSumEnum.values().length; i++) {
				dishNames[i] = new SelectItem(LargeSizedDimSumEnum.values()[i].getName());
			}
			setDishNames(dishNames);
			setSelectedDishPrice(LargeSizedDimSumEnum.values()[0].getPrice());
		} else if(DishTypeEnum.SPECIAL_DISHES.getCode().equals(selectedDishType)) {
			SelectItem[] dishNames = new SelectItem[SpecialDimSumEnum.values().length];
			for(int i = 0; i < SpecialDimSumEnum.values().length; i++) {
				dishNames[i] = new SelectItem(SpecialDimSumEnum.values()[i].getName());
			}
			setDishNames(dishNames);
			setSelectedDishPrice(SpecialDimSumEnum.values()[0].getPrice());
		} else if(DishTypeEnum.UNIQUE_DISHES.getCode().equals(selectedDishType)) {
			SelectItem[] dishNames = new SelectItem[UniqueDimSumEnum.values().length];
			for(int i = 0; i < UniqueDimSumEnum.values().length; i++) {
				dishNames[i] = new SelectItem(UniqueDimSumEnum.values()[i].getName());
			}
			setDishNames(dishNames);
			setSelectedDishPrice(UniqueDimSumEnum.values()[0].getPrice());
		}
	}
	
	public void addToOrder() {
		boolean isOrdered = false;
		for(OrderItem item: orderedItems) {
			if(item.getType().equals(selectedDishType) && item.getName().equals(selectedDishName)) {
				item.setQuantity(item.getQuantity() + selectedDishQuantity);
				isOrdered = true;
				break;
			}
		}
		if(!isOrdered) {
			OrderItem item = new OrderItem(selectedDishType, selectedDishName, selectedDishPrice, selectedDishQuantity);
			orderedItems.add(item);
		}
		
	}
	
//	public void removeItem(int rowIndex) {
//		orderedItems.remove(rowIndex);
//	}
	
	public void removeItem(OrderItem item) {
		orderedItems.remove(item);
	}
	
	public void validateQuantity(FacesContext fc, UIComponent c, Object value) {
		int quantity = (Integer) value;
		if (quantity <= 0) {
			throw new ValidatorException(new FacesMessage(
					"Quantity need to be a positive integer"));
		}
	}
	
	public String submit() {
		if(!validate(orderedItems)) {
			FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "The order is empty",
                    "Please try again!"));
 
            return "";
		}
		ProcessingOrder order = orderService.assembleProcessingOrder(FacesUtil.getUserName(), tableNo, orderedItems);
		orderService.saveProcessingOrder(order);
//		initOrder();
		return "order";
	}
	
	private boolean validate(List<OrderItem> items) {
		for(OrderItem item : items) {
			if(item.getQuantity() > 0) {
				return true;
			}
		}
		return false;
	}


	public String viewOrders() {
		return "processing";
	}
	
	public byte getTableNo() {
		return tableNo;
	}


	public void setTableNo(byte tableNo) {
		this.tableNo = tableNo;
	}


	public String getSelectedDishType() {
		return selectedDishType;
	}
	public void setSelectedDishType(String selectedDishType) {
		this.selectedDishType = selectedDishType;
	}
	public SelectItem[] getDishNames() {
		return dishNames;
	}
	public void setDishNames(SelectItem[] dishNames) {
		this.dishNames = dishNames;
	}
	public String getSelectedDishName() {
		return selectedDishName;
	}
	public void setSelectedDishName(String selectedDishName) {
		this.selectedDishName = selectedDishName;
	}
	public BigDecimal getSelectedDishPrice() {
		return selectedDishPrice;
	}
	public void setSelectedDishPrice(BigDecimal selectedDishPrice) {
		this.selectedDishPrice = selectedDishPrice;
	}
	public int getSelectedDishQuantity() {
		return selectedDishQuantity;
	}
	public void setSelectedDishQuantity(int selectedDishQuantity) {
		this.selectedDishQuantity = selectedDishQuantity;
	}
	public SelectItem[] getDishTypes() {
		return dishTypes;
	}

	public List<OrderItem> getOrderedItems() {
		return orderedItems;
	}
	
//	public DataModel getOrderedItems() {
//		return orderItemDataModel;
//	}
	
	public void setOrderService(OrderService orderService) {
		this.orderService = orderService;
	}
}
