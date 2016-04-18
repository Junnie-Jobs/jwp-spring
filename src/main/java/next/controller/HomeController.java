package next.controller;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import next.dao.QuestionDao;

//annotation기반으로 컨트롤러 등록하면 스프링빈으로 자동으로 등록된다.
@Controller
public class HomeController {
	private QuestionDao questionDao = QuestionDao.getInstance();
	// url매핑은 requestMapping을 이용하면된다. 그동안엔 url 하나에 get post를 구분할 수 없었는데
	// 스프링에서는 request method를 이용해서 구분할 수 있다.
	// 이게 가장 기본적인 모습의 mvc를 이용하는 모습이다.

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public ModelAndView home() throws Exception {
		ModelAndView mav = new ModelAndView("index");
		mav.addObject("questions", questionDao.findAll());
		return mav;
	}
}

// controller annotation으로 설정되어있는데,
// 패키지를 컴포넌트 스캔으로 읽고 같은 값으로 읽