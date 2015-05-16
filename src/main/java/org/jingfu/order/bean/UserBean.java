package org.jingfu.order.bean;

import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import org.jingfu.order.enums.UserRoleEnum;
import org.jingfu.order.model.AuthUser;
import org.jingfu.order.service.LoginService;
import org.jingfu.order.util.FacesUtil;

@ManagedBean(name="user") // or @Named("user")
@SessionScoped
public class UserBean implements Serializable {
   private String userName;
   private String password;
   private String role;

@ManagedProperty(value="#{loginService}")
   private LoginService loginService;

   public String getUserName() { return userName; }   
   public void setUserName(String userName) { this.userName = userName; }

   public String getPassword() { return password; }
   public void setPassword(String newValue) { password = newValue; }
   
   public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	
	public String login() {
		AuthUser user = loginService.checkLoginInfo(userName, password);
		if(user != null) {
			// get Http Session and store username
            HttpSession session = FacesUtil.getSession();
            session.setAttribute("username", user.getUserName());
            session.setAttribute("role", user.getRole());
            setRole(user.getRole());
            if(UserRoleEnum.CHEF.getName().equals(role)) {
            	return "processing";
            } else {
            	return "order";
            }
            
		} else {
			FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Invalid user name or password!",
                    "Please try again!"));
 
            return "";
		}
	}
		
	public String logout() {
		HttpSession session = FacesUtil.getSession();
		session.invalidate();
		return "login";
	}
	
	public void setLoginService(LoginService loginService) {
		this.loginService = loginService;
	}
   
}
