package org.jingfu.order.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.jingfu.order.util.HibernateUtil;

public class HibernateListener implements ServletContextListener {

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		HibernateUtil.getSessionfactory().close();
		
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		HibernateUtil.getSessionfactory();
		
	}

}
