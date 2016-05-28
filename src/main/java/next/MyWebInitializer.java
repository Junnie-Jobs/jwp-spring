package next;

import java.util.EnumSet;

import javax.servlet.DispatcherType;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

import next.config.AppConfig;
import next.config.WebMvcConfig;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.filter.HiddenHttpMethodFilter;
import org.springframework.web.servlet.DispatcherServlet;

import com.navercorp.lucy.security.xss.servletfilter.XssEscapeServletFilter;

public class MyWebInitializer implements WebApplicationInitializer {
	@Override
	public void onStartup(ServletContext servletContext) throws ServletException {
		AnnotationConfigWebApplicationContext appContext = new AnnotationConfigWebApplicationContext();
		appContext.register(AppConfig.class);
		servletContext.addListener(new ContextLoaderListener(appContext));
		
		CharacterEncodingFilter cef = new CharacterEncodingFilter();
		cef.setEncoding("UTF-8");
		cef.setForceEncoding(true);
		XssEscapeServletFilter xesf = new XssEscapeServletFilter();
		servletContext.addFilter("xssPreventerDefender", xesf).addMappingForUrlPatterns(null, false, "/*");
		servletContext.addFilter("xssSaxFilterDefender", xesf).addMappingForUrlPatterns(null, false, "/*");
		servletContext.addFilter("xssFilterDefender", xesf).addMappingForUrlPatterns(null, false, "/*");
		servletContext.addFilter("characterEncodingFilter", cef).addMappingForUrlPatterns(null, false, "/*");
		servletContext.addFilter("httpMethodFilter", HiddenHttpMethodFilter.class)
				.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), false, "/*");

		AnnotationConfigWebApplicationContext webContext = new AnnotationConfigWebApplicationContext();
		webContext.setParent(appContext);
		webContext.register(WebMvcConfig.class);
		ServletRegistration.Dynamic dispatcher = servletContext.addServlet("next", new DispatcherServlet(webContext));
		dispatcher.setLoadOnStartup(1);
		dispatcher.addMapping("/");
		
		
		//webInitializer에서 web.xml에서 하는 부분을 사용해준다.
	}
}
