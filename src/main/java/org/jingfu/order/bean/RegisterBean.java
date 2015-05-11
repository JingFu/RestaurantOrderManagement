package org.jingfu.order.bean;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import org.jingfu.order.enums.UserRoleEnum;
import org.jingfu.order.service.LoginService;

@ManagedBean(name="register") 
@RequestScoped
public class RegisterBean {
	private String userName;
	private String password;
	private String role;
	private SelectItem[] roles;
	@ManagedProperty(value="#{loginService}")
    private LoginService loginService;
	
	public RegisterBean() {
		roles = new SelectItem[UserRoleEnum.values().length];
		for(int i = 0; i < UserRoleEnum.values().length; i++) {
			roles[i] = new SelectItem(UserRoleEnum.values()[i].getName());
		}
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public void setLoginService(LoginService loginService) {
		this.loginService = loginService;
	}
	
	public SelectItem[] getRoles() {
		return roles;
	}
	public String createUser() {
		boolean isValid = validateUserName(userName);
		if(isValid) {
			loginService.register(userName, password, role);
			return "register_success";
		} else {
			FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "User name already exists",
                    "Please use another name"));
			return "";
		}
	}
		
	
	private boolean validateUserName(String userName) {
		return loginService.validateUserName(userName);
	}

}
