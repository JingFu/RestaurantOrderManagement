package org.jingfu.order.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.jingfu.order.enums.UserRoleEnum;

public class LoginCheckFilter implements Filter {
	private Map<String, List<String>> roleAndPageMap = new HashMap<String, List<String>>();
	

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        HttpSession session = req.getSession(false);
        boolean isLogin = session != null && session.getAttribute("username") != null;
        //  allow user to proccede if url is index.xhtml or user logged in
        String reqURI = req.getRequestURI();
        if("/OrderManagement/".equals(reqURI) || reqURI.contains("index.xhtml")) {
        	if(isLogin) {
        		// already login, direct to function page by user role
        		String role = (String) session.getAttribute("role");
        		if(UserRoleEnum.WAITING_STAFF.getName().equals(role)) {
        			res.sendRedirect(req.getContextPath() + "/secured/order.xhtml");
        		} else if(UserRoleEnum.CHEF.getName().equals(role)) {
        			res.sendRedirect(req.getContextPath() + "/secured/orderProcessing.xhtml");
        		} else {
        			res.sendRedirect(req.getContextPath() + "/secured/order.xhtml");
        		}
        		
        	} else {
        		chain.doFilter(request, response);
        	}
        } else {
        	if(isLogin) {
        		String role = (String) session.getAttribute("role");
        		if(UserRoleEnum.WAITING_STAFF.getName().equals(role) || UserRoleEnum.CHEF.getName().equals(role)) {
        			if(! roleAndPageMap.get(role).contains(reqURI)) {
        				 res.sendRedirect(req.getContextPath() + "/index.xhtml"); 
        				 return;
        			}
        		}
        		chain.doFilter(request, response);
        	} else {
        		// user didn't log in but asking for a page that is not allowed, so direct user to login page
                res.sendRedirect(req.getContextPath() + "/index.xhtml"); 
        	}
        	
        }
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		List<String> waitingStaffPages = new ArrayList<String>();
		waitingStaffPages.add("/OrderManagement/secured/order.xhtml");
		waitingStaffPages.add("/OrderManagement/secured/orderDelivery.xhtml");
		roleAndPageMap.put(UserRoleEnum.WAITING_STAFF.getName(), waitingStaffPages);
		List<String> chefPages = new ArrayList<String>();
		chefPages.add("/OrderManagement/secured/orderProcessing.xhtml");
		roleAndPageMap.put(UserRoleEnum.CHEF.getName(), chefPages);
		
	}

}
