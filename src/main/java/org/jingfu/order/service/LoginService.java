package org.jingfu.order.service;

import java.io.Serializable;
import java.util.List;

import org.jingfu.order.model.AuthUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;


@Service
@Scope("singleton")
public class LoginService implements Serializable {
	private static final long serialVersionUID = 1L;
	@Autowired
	private SessionManager sessionManager;
	public LoginService() {
		System.out.println("================================");
		System.out.println("LoginService Bean is initialized");
		System.out.println("================================");
	}
	public AuthUser checkLoginInfo(String userName, String password) {
		List<AuthUser> users = sessionManager.getAllUserInfo();
		for(AuthUser user : users) {
			if(user.getUserName().equals(userName) && user.getPassword().equals(password)) {
				return user;
			}
		}
		return null;
	}
	public void register(String userName, String password, String role) {
		AuthUser user = new AuthUser(userName, password, role);
		sessionManager.createUser(user);
	}
	public boolean validateUserName(String userName) {
		return !sessionManager.isUserExist(userName);
		
	}
}
