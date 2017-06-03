package org.mabysh.familybudget.backend.config;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

import com.vaadin.spring.server.SpringVaadinServlet;

public class WebContextInitializer implements WebApplicationInitializer {

	@Override
	public void onStartup(ServletContext servletContext) throws ServletException {
		AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
		context.scan(WebContextInitializer.class.getPackage().getName());
		servletContext.addListener(new ContextLoaderListener(context));
		registerServlet(servletContext);
	}

	private void registerServlet(ServletContext servletContext) {
		ServletRegistration.Dynamic dispatcher = servletContext.addServlet("vaadin",
				SpringVaadinServlet.class);
		dispatcher.setLoadOnStartup(1);
		dispatcher.addMapping("/*");
		dispatcher.setAsyncSupported(true);
	}
}
