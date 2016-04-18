package next.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

//springmvc를 쓰기 위해서는 MyWebInitializer와 WebMvcConfig는 개발자가 만들어야 한다. 

@Configuration
@EnableWebMvc // springmvc를 쓰려면 이 annotation을 써야한다.
@ComponentScan(basePackages = { "next.controller" })
// compenentscan을 통해 특정 패키지의 클래스만 찾아서 빈 클래스를 등록할 수 있다. 패키지가 여러개라면 배열형식으로 설정할 수
// 있다.
// WebMvcConfig 실제로 스프링 mvc초기화 및 설정을 담당한다.
public class WebMvcConfig extends WebMvcConfigurerAdapter {
	private static final int CACHE_PERIOD = 31556926; // one year
	// 캐시를 준 이유는 무엇일까? 성능개선이나 부분에서 이야기하는건데 이 설정을 하게 되면 어떤 의미냐면
	// 자바스크립트나 이미지 이런 파일은 잘 안바뀌는데, 잘생각해보면 브라우저에서 매번 서버로 요청을 보내는건
	// 성능상 좋지 않다. 그래서 기본적으로 브라우저에서 웹서버로 요청하게 되면 많은 자원을 다운로딩하고 렌더링하는데
	// 자바스크립트나 css는 잘 안바뀌므로 여러분 설정에 따라서 브라우저가 로컬에 다운받고 그 파일을 캐싱하고 있다.
	// 이 설정을 잘하면 엄청난 성능개선효과를 본다.
	// 그런데 많은 서비스들이 그런 설정을 안하고 있다. 그것에 대한 설정이다. 위의 설정은 1년을 잡은 것인데 필요에 따라 조정하면
	// 된다.

	// 디폴트로 jsp를 서비스하는데 webapp디렉토리 바로 밑에 두는게 아니라 WEB-INF밑에 빼면
	// 아래와 같은 방식으로 prefix와 suffix를 이용해 더 짧게 설정을 짧게 할 수 있다.
	@Bean
	public ViewResolver viewResolver() {
		InternalResourceViewResolver bean = new InternalResourceViewResolver();
		bean.setViewClass(JstlView.class);
		bean.setPrefix("/WEB-INF/jsp/");
		bean.setSuffix(".jsp");
		return bean;
	}

	// 정적인 자원인 파일들을 web-inf밑에서 관리할 수 있다. 요청 url을 매핑할 수 있다.
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/resources/**")// resources로 들어오는 모든 url은
													// 정적인 파일이구
				.addResourceLocations("/WEB-INF/static_resources/").setCachePeriod(CACHE_PERIOD);
	}

	// 이 설정은 일단 해야한다고 생각해두자.
	@Override
	public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
		// Serving static files using the Servlet container's default Servlet.
		configurer.enable();
	}
}
