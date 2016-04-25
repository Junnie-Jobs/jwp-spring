package next.controller;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class HomeControllerTest {

	private AnnotationConfigApplicationContext ac;

//	@Test
//	 public void getBean() throws Exception {
//	 AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(ApplicationConfig.class);
//	 HomeController h1 = ac.getBean(HomeController.class);
//	 System.out.println(mp.getMessage());
//	 ac.close();
//	 }
	
	@Before
	public void setup() {

		ac = new AnnotationConfigApplicationContext(ApplicationConfig.class);
	}

	@Test
	public void equalsBean() throws Exception {

		HomeController hc1 = ac.getBean(HomeController.class);
		HomeController hc2 = ac.getBean(HomeController.class);
		assertTrue(hc1 == hc2);
	}

	@After
	public void teardown() {

		ac.close();

	}

}
