package next.controller.user;


import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import next.controller.UserSessionUtils;
import next.dao.UserDao;
import next.model.User;

@Controller
public class UsersController {

	private static final Logger log = LoggerFactory.getLogger(UsersController.class);
	
	@Resource
	private UserDao userDao;

	// 1.CreateUserController

	// membership page mapping
	@RequestMapping(value = "/users/form", method = RequestMethod.GET)
	public ModelAndView form() throws Exception {
		ModelAndView mav = new ModelAndView("user/form");
		return mav;
	}

	@RequestMapping(value = "/users/create", method = RequestMethod.POST)
	public String execute(@RequestParam String userId, @RequestParam String password, @RequestParam String name,
			@RequestParam String email) throws Exception {
		User user = new User(userId, password, name, email);
		log.debug("User : {}", user);
		userDao.insert(user);
		return "redirect:/";
	}
	


	// 2.LoginUserController

	// loginPage mapping
	@RequestMapping(value = "/users/loginForm", method = RequestMethod.GET)
	public ModelAndView loginForm() throws Exception {
		ModelAndView mav = new ModelAndView("user/login");
		return mav;
	}

	@RequestMapping(value = "/users/login", method = RequestMethod.POST)
	public String execute(HttpSession session, @RequestParam String userId, @RequestParam String password)
			throws Exception {

		User user = userDao.findByUserId(userId);

		if (user == null) {
			throw new NullPointerException("사용자를 찾을 수 없습니다.");
		}

		if (user.matchPassword(password)) {

			session.setAttribute("user", user);
			return "redirect:/";
		} else {
			throw new IllegalStateException("비밀번호가 틀립니다.");
		}
	}

	// 3.LogoutUserController

	@RequestMapping(value = "/users/logout", method = RequestMethod.GET)
	public String execute(HttpSession session) throws Exception {
		session.removeAttribute("user");
		return "redirect:/";
	}

	// 4.ListUserController

	@RequestMapping(value = "/users", method = RequestMethod.GET)
	public ModelAndView userList(HttpSession session) throws Exception {

		if (!UserSessionUtils.isLogined(session)) {

			ModelAndView mav = new ModelAndView("/user/login");
			return mav;
		}

		ModelAndView mav = new ModelAndView("user/list");
		// 잠깐 이 users attribute는 어디서 나오는걸까??
		mav.addObject("users", userDao.findAll());

		return mav;
	}

	// 5.ProfileController

	@RequestMapping(value = "/users/profile", method = RequestMethod.GET)
	public ModelAndView profile(HttpSession session, @RequestParam String userId) throws Exception {

		if (!UserSessionUtils.isLogined(session)) {

			ModelAndView mav = new ModelAndView("/user/login");
			return mav;
		}

		ModelAndView mav = new ModelAndView("/user/profile");
		mav.addObject("user", userDao.findByUserId(userId));
		return mav;
	}
	
	// 6.UpdateFormUserController
	
	@RequestMapping(value = "/users/updateForm", method = RequestMethod.GET)
	public ModelAndView updateForm(HttpSession session, @RequestParam String userId) throws Exception {

		if (!UserSessionUtils.isLogined(session)) {
			throw new IllegalStateException("다른 사용자의 정보를 수정할 수 없습니다.");
		}

		ModelAndView mav = new ModelAndView("/user/updateForm");
		mav.addObject("user", userDao.findByUserId(userId));
		return mav;
	}
	
	// 7.UpdateUserController
	
	@RequestMapping(value = "/users/update", method = RequestMethod.POST)
	public ModelAndView updateUser(HttpSession session, @RequestParam String userId, @RequestParam String password, @RequestParam String name,
			@RequestParam String email) throws Exception {

		if (!UserSessionUtils.isLogined(session)) {
			throw new IllegalStateException("다른 사용자의 정보를 수정할 수 없습니다.");
		}

		User user = userDao.findByUserId(userId);
		User updateUser = new User(userId, password, name, email);	       
	    log.debug("Update User : {}", updateUser);
	    user.update(updateUser);
	    
	    ModelAndView mav = new ModelAndView("redirect:/");
	    return mav;
	}
	
}