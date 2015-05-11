package org.jingfu.order.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LoginCheckFilter implements Filter {

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
        		// already login, direct to function page
        		res.sendRedirect(req.getContextPath() + "/secured/order.xhtml");
        	} else {
        		chain.doFilter(request, response);
        	}
        } else {
        	if(isLogin) {
        		chain.doFilter(request, response);
        	} else {
        		// user didn't log in but asking for a page that is not allowed, so direct user to login page
                res.sendRedirect(req.getContextPath() + "/index.xhtml"); 
        	}
        	
        }
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub
		
	}

}
